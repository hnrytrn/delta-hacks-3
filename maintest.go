package main

import (
	"encoding/json"
	"log"
	"strings"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/itsjamie/gin-cors"
	"github.com/tarm/serial"
)

func main() {
	arduino, err := serial.OpenPort(&serial.Config{Name: "COM4", Baud: 9600})
	panicif(err)

	sensorOutput := make([]float32, 6)
	steps := 0

	// server stuff here
	router := gin.Default()

	router.Use(cors.Middleware(cors.Config{
		Origins:         "*",
		Methods:         "GET, PUT, POST, DELETE",
		RequestHeaders:  "Origin, Authorization, Content-Type",
		ExposedHeaders:  "",
		MaxAge:          50 * time.Second,
		Credentials:     true,
		ValidateHeaders: false,
	}))

	router.GET("/", func(c *gin.Context) {
		c.JSON(200, sensorOutput)
	})

	router.GET("/steps", func(c *gin.Context) {
		c.JSON(200, steps)
	})

	go router.Run()

	// [broken, outside ball, heel bottom, inside ball, tip toes, arch]
	var rawOutput string
	stepping := false
	for {
		buf := make([]byte, 128)
		n, err := arduino.Read(buf)
		panicif(err)

		rawOutput += string(buf[:n])

		if strings.Contains(rawOutput, "\n") {
			start := strings.Index(rawOutput, "[")
			if start == -1 {
				rawOutput = ""
				continue
			}

			json.Unmarshal([]byte(rawOutput)[:strings.Index(rawOutput, "\n")], &sensorOutput)
			rawOutput = ""

			heelPressure := sensorOutput[2]

			if heelPressure > 900 {
				stepping = true
			} else if heelPressure < 600 {
				if stepping {
					steps++
				}
				stepping = false
			}
		}
	}
}

func nearTen(input int, val int) bool {
	return (val-10 <= input) && (val+10 >= input)
}

func panicif(err error) {
	if err != nil {
		log.Fatal(err)
	}
}

func mustMarshal(data interface{}) []byte {
	out, err := json.Marshal(data)
	if err != nil {
		panic(err)
	}

	return out
}
