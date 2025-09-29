# Stock Sync Service

Spring Boot microservice that syncs product stock from multiple vendors.

## 🚀 Quick Start

### Run with maven
```bash

# 1. Unzip and run
unzip stock-sync-solution.zip
cd stock-sync-solution
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
### 2. Monitor Real-time Operation
#### Watch console logs for:
- Auto-created sample CSV file
- Vendor API calls and retries
- Out-of-stock alerts
- Sync completion summaries

### 3. View Database
- URL: http://localhost:8080/h2-console
- JDBC: jdbc:h2:mem:stocksync
- Username: sa (no password)

## 🏗 Design Highlights
### Architecture
- Clean Layers: Controller → Service → Repository
- Modular Design: Easy to add new vendors
- Error Handling: Graceful degradation, detailed logging
- 
### Data Model Design
- SKU Handling: Same SKU can exist across vendors, application uses (sku + vendor) as business key
- Current Implementation: Uniqueness enforced at application layer through service logic
- Production Enhancement: Can add `@UniqueConstraint(columnNames = {"sku", "vendor"})` for database-level integrity
- Real-world Alignment: Fully supports the business requirement of same SKU across suppliers

### Production Ready Features
- Health Check: http://localhost:8080/health
- Comprehensive Tests: Core logic and API endpoints
- Containerized: Full Docker support

## 🎯 Design Decisions & Trade-offs
### Assumptions Made
- Data Scale: Optimized for hundreds of products per vendor
- Sync Frequency: 1-minute intervals balance data freshness and system load
- Vendor Reliability: Built retry mechanisms assuming temporary API failures
- Concurrency: Single instance deployment, no distributed lock needed for current scale

### Technical Trade-offs
- H2 Database: Chosen for evaluation simplicity over production databases
- In-Memory Processing: Suitable for current scale, prepared for batch processing
- Log-based Alerts: Immediate visibility vs. persisted event storage
- Synchronous Processing: Meets current requirements, async pattern prepared for scaling

### Production Readiness
- Easy Database Switch: Configuration ready for PostgreSQL/MySQL
- Monitoring Ready: Structured logs for alert integration
- Extension Points: Clear patterns for adding new vendors and features

### Focus Areas
- Delivered: All core requirements with production-quality code
- Deferred: Advanced features like distributed caching, message queues
- Designed For: Easy extension and maintenance

## 📊 Git History
```bash

git log --oneline
# Shows progressive commits from setup to completion
```

## 💡 For the Interview
#### I'll walk through:
- Out-of-stock detection logic
- Retry mechanism for API failures
- Easy extension for new vendors

### Ready for technical discussion!
