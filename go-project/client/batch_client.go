package client

import (
	"bytes"
	"fmt"
	"io"
	"net/http"
	"sync"
	"time"
)

type BatchClientInterface interface {
	Run() []error
}

type Config struct {
	Method    string
	URL       string
	Body      string
	BatchSize int
	Steps     int
	Timeout   time.Duration
}

type BatchClient struct {
	cfg    Config
	client *http.Client
	errors []error
}

func New(cfg Config) BatchClientInterface {
	return &BatchClient{
		cfg: cfg,
		client: &http.Client{
			Timeout: cfg.Timeout,
		},
	}
}

func (b *BatchClient) sendRequest(index int, wg *sync.WaitGroup, errCh chan<- string) {
	defer wg.Done()

	var body io.Reader
	if b.cfg.Body != "" && (b.cfg.Method == "POST" || b.cfg.Method == "PUT" || b.cfg.Method == "PATCH") {
		body = bytes.NewBuffer([]byte(b.cfg.Body))
	}

	req, err := http.NewRequest(b.cfg.Method, b.cfg.URL, body)
	if err != nil {
		errCh <- fmt.Sprintf("[Request %d] Request creation failed: %v", index, err)
		return
	}

	if body != nil {
		req.Header.Set("Content-Type", "application/json")
	}

	resp, err := b.client.Do(req)
	if err != nil {
		errCh <- fmt.Sprintf("[Request %d] Error: %v", index, err)
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode >= 400 {
		errCh <- fmt.Sprintf("[Request %d] HTTP %s: %s", index, b.cfg.Method, resp.Status)
	} else {
		fmt.Printf("[Request %d] HTTP %s: %s\n", index, b.cfg.Method, resp.Status)
	}
}

func (b *BatchClient) runBatch() {
	for step := 0; step < b.cfg.Steps; step++ {
		fmt.Printf("---- Step %d ----\n", step+1)

		var wg sync.WaitGroup
		errCh := make(chan string, b.cfg.BatchSize)
		wg.Add(b.cfg.BatchSize)

		for i := 0; i < b.cfg.BatchSize; i++ {
			go b.sendRequest(step*b.cfg.BatchSize+i+1, &wg, errCh)
		}

		wg.Wait()
		close(errCh)

		for msg := range errCh {
			b.errors = append(b.errors, fmt.Errorf(msg))
			fmt.Println("ERROR:", msg)
		}
		fmt.Printf("---- Step %d complete ----\n\n", step+1)
	}
}

func (b *BatchClient) Run() []error {
	start := time.Now()

	b.runBatch()

	fmt.Printf("Completed in %s\n", time.Since(start))
	return b.errors
}
