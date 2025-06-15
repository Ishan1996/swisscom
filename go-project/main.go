package main

import (
	"flag"
	"fmt"
	"os"
	"strings"
	"time"

	"go-project/client"
)

func main() {
	batchSize := flag.Int("batch", 100, "Number of parallel requests per batch")
	steps := flag.Int("steps", 10, "Number of steps (batches)")
	url := flag.String("url", "http://localhost:8080/api/services", "Target URL")
	method := flag.String("method", "GET", "HTTP method: GET, POST, PUT, DELETE, PATCH")
	body := flag.String("body", "", "Inline JSON body")
	timeout := flag.Int("timeout", 10, "HTTP request timeout in seconds")

	flag.Parse()

	*method = strings.ToUpper(*method)

	if *batchSize <= 0 || *steps <= 0 {
		fmt.Println("Error: batch and steps must be positive integers")
		os.Exit(1)
	}

	if *batchSize > 100 {
		fmt.Println("Error: batch size must not exceed 100")
		os.Exit(1)
	}

	if *steps > 10 {
		fmt.Println("Error: steps must not exceed 10")
		os.Exit(1)
	}

	if *url == "" {
		fmt.Println("Error: URL must be provided")
		os.Exit(1)
	}

	if *method != "GET" && *method != "POST" && *method != "PUT" && *method != "DELETE" && *method != "PATCH" {
		fmt.Println("Error: Invalid HTTP method. Supported methods are: GET, POST, PUT, DELETE, PATCH")
		os.Exit(1)
	}

	var payload string
	if body != nil && *body != "" {
		payload = *body
	}

	cfg := client.Config{
		Method:    *method,
		URL:       *url,
		Body:      payload,
		BatchSize: *batchSize,
		Steps:     *steps,
		Timeout:   time.Duration(*timeout) * time.Second,
	}

	c := client.New(cfg)
	c.Run()
}
