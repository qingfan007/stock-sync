# Stock Sync Service

Spring Boot microservice that synchronizes product stock from multiple vendors.

## 🚀 Quick Start

### Run with maven
```bash

# 1. Unzip and run
git clone https://github.com/qingfan007/stock-sync.git
cd stock-sync
mvn spring-boot:run

# 2. Test it works (new terminal)
curl http://localhost:8080/api/products
```
Or with Docker
```bash

docker-compose up --build
# Access: http://localhost:8081/api/products
```
## ✅ What's Built
### Core Features
- ✅ Vendor A: REST API integration with auto-retry (3 attempts)
- ✅ Vendor B: CSV file processing, auto-creates sample data
- ✅ Scheduled Sync: Runs every minute automatically
- ✅ Out-of-Stock Detection: Logs warnings when stock hits zero
- ✅ REST API: Complete product query endpoints
- ✅ Database: H2 with web console at http://localhost:8080/h2-console
- ✅ Data Persistence: All product data stored and queryable
- ✅ API Documentation: Swagger/OpenAPI at http://localhost:8080/swagger-ui/index.html
- ✅ Test Coverage: Unit & integration tests for core logic

### Enhanced Features
- ✅ Manual Sync Triggers: REST endpoints for on-demand synchronization
- ✅ Flexible Operations: Full sync or vendor-specific sync options

## 🛠 Vendor Simulation
### Vendor A - REST API
- Mock Endpoint: http://localhost:8080/mock/vendor-a/stock
- Returns: JSON product data with stock quantities
- Features: Built-in retry logic for reliability
### Vendor B - CSV File
- Location: /tmp/vendor-b/stock.csv (auto-created)
- Format: sku,name,stockQuantity
- Simulates: FTP file drop scenario

## 🧪 Test the Solution
### 1. Verify Core Features
```bash

# Check all products
curl http://localhost:8080/api/products

# Run all tests
mvn test
```
- Console logs show vendor sync attempts, retries, CSV auto-generation, out-of-stock alerts.
- Database can be inspected via H2 console.

## 🏗 Design Highlights
### Architecture
- Clean Layers: Controller → Service → Repository
- Modular Design: Easy to add new vendors
- Error Handling: Graceful degradation, detailed logging
- 
### Data Model Design
- Business Key: (sku + vendor) ensures uniqueness
- Uniqueness currently enforced at service layer
- Ready for DB-level constraint via @UniqueConstraint

### Production Ready Features
- Health Check: /health
- Comprehensive Tests: Core logic and API endpoints
- Containerized: Full Docker support

## 🔮 Future Improvements
- Distributed caching (e.g. Redis)
- Message queues (e.g. Kafka) for async processing
- Persisted out-of-stock event tracking
- Multi-instance deployment with distributed locks

## 📊 Git History
```bash

git log --oneline
# Shows progressive commits from setup to completion
```
## 📖 Documentation
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- H2 Console: http://localhost:8080/h2-console