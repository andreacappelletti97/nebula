# Nebula
Dynamic Generation and Management of Actor-Based Applications

## Author
Andrea Cappelletti  
andreacappelletti97@gmail.com

Dr. Mark Grechanik  
drmark@uic.edu

## 1) Cinnamon telemetry
Prerequisites
- Docker
```bash
cd docker
```

Run the dashboard

```bash
docker compose-up
```
Go to localhost:3000/


## yml2dot
Install Go
```bash
brew install go
```

Install Graphviz

```bash
brew install graphviz
```

Run
```bash
./yml2dot deployment.yml | dot -Tpng > deployment.png
```





