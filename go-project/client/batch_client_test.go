package client

import (
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func mockServer() *httptest.Server {
	handler := http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodGet:
			w.WriteHeader(http.StatusOK)
			w.Write([]byte(`{"message": "ok"}`))
		case http.MethodPost:
			w.WriteHeader(http.StatusCreated)
			w.Write([]byte(`{"message": "created"}`))
		default:
			http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		}
	})
	return httptest.NewServer(handler)
}

func TestBatchClient_RunBatchRequests(t *testing.T) {
	server := mockServer()
	defer server.Close()

	var body string
	timeout := 10
	cfg := Config{
		URL:       server.URL,
		Method:    "GET",
		Body:      body,
		Steps:     2,
		BatchSize: 5,
		Timeout:   time.Duration(timeout) * time.Second,
	}

	c := New(cfg)

	errors := c.Run()

	assert.Equal(t, 0, len(errors), "Expected no errors, got %d", len(errors))
}

func TestBatchClient_ErrorHandling(t *testing.T) {
	timeout := 10
	cfg := Config{
		URL:       "http://127.0.0.1:12345",
		Method:    "GET",
		Steps:     1,
		BatchSize: 5,
		Timeout:   time.Duration(timeout) * time.Second,
	}

	c := New(cfg)

	errors := c.Run()

	assert.Greater(t, len(errors), 0, "Expected errors, got none")
}
