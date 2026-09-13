# Spring-Docker

A hands-on project for understanding how to containerize a **Spring Boot** application using **Docker** — covering the core concepts, the `Dockerfile` and `docker-compose.yaml` used in this repo, and the everyday Docker commands needed to build, run, and ship the app.

## Table of Contents

- [What Is Docker?](#what-is-docker)
- [Why Use Docker?](#why-use-docker)
- [Docker Architecture](#docker-architecture)
- [What Is a Dockerfile?](#what-is-a-dockerfile)
- [What Is docker-compose.yaml?](#what-is-docker-composeyaml)
- [Common Docker Commands](#common-docker-commands)
- [Containerizing This Project](#containerizing-this-project)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)

## What Is Docker?

Docker is a platform for building, packaging, and running applications inside **containers** — lightweight, isolated environments that bundle an application together with everything it needs to run (code, runtime, libraries, system tools, and settings).

Instead of installing Java, Maven, and every dependency directly on a machine and hoping it behaves the same way everywhere, you package the application once into a container **image**, and that same image runs identically on your laptop, a teammate's machine, or a production server.

## Why Use Docker?

- **"It works on my machine" — solved.** The container includes the exact runtime and dependencies the app needs, so behavior doesn't change between environments.
- **Consistency across environments.** Dev, test, and production all run the same image, eliminating environment-drift bugs.
- **Isolation.** Each container runs independently, so dependency conflicts between apps (e.g. two apps needing different Java versions) disappear.
- **Portability.** An image built once can run on any machine with Docker installed — Windows, macOS, Linux, or any cloud provider.
- **Faster startup than virtual machines.** Containers share the host OS kernel instead of virtualizing an entire OS, so they start in seconds and use fewer resources.
- **Easier scaling and deployment.** Spinning up more instances of a service is as simple as running more containers from the same image — a natural fit for microservices.
- **Simplified onboarding.** A new developer can get the whole stack running with a single `docker-compose up` instead of manually installing and configuring every dependency.

## Docker Architecture (Detailed Component & Runtime Architecture)

Docker follows a client-server architecture:

<img width="1408" height="768" alt="image" src="https://github.com/user-attachments/assets/0a1a33a2-4963-46b7-a6c4-095f8a6a3cb9" />

## Docker Architecture (High-Level Client-Server (Logical) Architecture)

<img width="1408" height="768" alt="image" src="https://github.com/user-attachments/assets/21cb6096-18c0-4b86-be5b-14945ffbb4fd" />


**Key components:**

- **Docker Client** — the `docker` command-line tool (or API) you interact with. Every command you type (`docker build`, `docker run`, etc.) is sent to the daemon.
- **Docker Daemon (`dockerd`)** — the background service that does the actual work: building images, running containers, managing networks and volumes. It listens for requests from the client.
- **Docker Images** — read-only templates that define what goes into a container (base OS/runtime, application code, dependencies, config). Images are built in layers, and each layer is cached for faster rebuilds.
- **Docker Containers** — running instances of an image. A container is an image plus a writable layer on top, isolated from other containers via the host OS's namespaces and cgroups.
- **Docker Registry** — a storage/distribution service for images (e.g. **Docker Hub**, or a private registry). `docker push` uploads an image there; `docker pull` downloads one.
- **Networks & Volumes** — Docker-managed networking (so containers can talk to each other by name) and persistent storage (so data survives container restarts).

## What Is a Dockerfile?

A **Dockerfile** is a plain-text script of instructions that tells Docker how to build an image for this application — step by step, starting from a base image, copying in code, installing dependencies, and defining how the app should start.

A typical Dockerfile for a Spring Boot app looks like:

```dockerfile
# Use an official OpenJDK runtime as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
```

- `FROM` — sets the base image everything else builds on top of (here, a lightweight Java runtime).
- `WORKDIR` — sets the working directory inside the container for subsequent instructions.
- `COPY` — copies files from your machine into the image (here, the compiled `.jar`).
- `EXPOSE` — documents which port the container listens on.
- `ENTRYPOINT` / `CMD` — defines the command that runs when a container starts from this image.

> Check this repo's actual `Dockerfile` and adjust the explanation above if the base image, build steps, or entrypoint differ.

## What Is docker-compose.yaml?

While a `Dockerfile` defines **how to build one image**, `docker-compose.yaml` defines **how to run one or more containers together** — their configuration, ports, environment variables, networks, and dependencies on each other — all in a single declarative file.

Instead of remembering (and re-typing) a long `docker run` command with many flags, you describe the desired setup once and bring it all up with one command.

A typical `docker-compose.yaml` for this kind of project looks like:

```yaml
version: "3.8"

services:
  docker-service:
    build: .
    image: vishalkamaliya/docker-service
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    restart: unless-stopped
```

- `services` — lists each container this file manages (here, just `docker-service`).
- `build: .` — tells Compose to build the image from the `Dockerfile` in the current directory.
- `image` — the name/tag to give the built image.
- `ports` — maps a port on the host machine to a port inside the container (`host:container`).
- `environment` — environment variables passed into the container (e.g. active Spring profile).
- `restart` — restart policy if the container stops or the host reboots.

If the app depends on other services (a database, another microservice), they'd be added as additional entries under `services`, and Compose handles networking between them automatically.

> Check this repo's actual `docker-compose.yaml` and update the fields above (service name, ports, environment variables) to match exactly what's defined.

## Common Docker Commands

### Images

| Command | Description |
|---|---|
| `docker build -t <name> .` | Build an image from the Dockerfile in the current directory, tagged `<name>`. |
| `docker images` | List all images stored locally. |
| `docker rmi <image>` | Remove a local image. |
| `docker pull <image>` | Download an image from a registry (e.g. Docker Hub). |
| `docker push <image>` | Upload a locally built image to a registry. |
| `docker tag <image> <new-tag>` | Create an additional tag/name for an existing image. |

### Containers

| Command | Description |
|---|---|
| `docker run <image>` | Create and start a new container from an image. |
| `docker run -d -p 8080:8080 <image>` | Run a container in detached mode, mapping host port 8080 to container port 8080. |
| `docker ps` | List currently running containers. |
| `docker ps -a` | List all containers, including stopped ones. |
| `docker stop <container>` | Stop a running container. |
| `docker start <container>` | Start a previously stopped container. |
| `docker restart <container>` | Restart a container. |
| `docker rm <container>` | Remove a stopped container. |
| `docker logs <container>` | View the logs output by a container. |
| `docker exec -it <container> sh` | Open an interactive shell inside a running container. |

### Docker Compose

| Command | Description |
|---|---|
| `docker-compose up` | Build (if needed) and start all services defined in `docker-compose.yaml`. |
| `docker-compose up -d` | Same as above, but run in detached (background) mode. |
| `docker-compose down` | Stop and remove all containers and networks created by `up`. |
| `docker-compose build` | Build (or rebuild) the images for the services, without starting them. |
| `docker-compose logs -f` | Follow the combined logs of all running services. |
| `docker-compose ps` | List the status of services managed by this Compose file. |

### Cleanup / Housekeeping

| Command | Description |
|---|---|
| `docker system prune` | Remove unused containers, networks, and dangling images to free up space. |
| `docker volume ls` | List Docker-managed volumes. |
| `docker network ls` | List Docker-managed networks. |

## Containerizing This Project

The following steps package this Spring Boot app into a Docker image, publish it to Docker Hub, and run it via Compose:

1. **Build the application jar** (so the Dockerfile has something to copy in):
   ```bash
   ./mvnw clean package
   ```

2. **Build the Docker image** from the `Dockerfile`, tagging it for Docker Hub:
   ```bash
   docker build -t vishalkamaliya/docker-service .
   ```
   This reads the `Dockerfile` in the current directory and produces a local image named `vishalkamaliya/docker-service`.

3. **Push the image to Docker Hub** so it can be pulled and run anywhere:
   ```bash
   docker push vishalkamaliya/docker-service
   ```
   > Requires being logged in first via `docker login`, and that `vishalkamaliya/docker-service` is a repository you own/have push access to on Docker Hub.

4. **Start the service via Docker Compose:**
   ```bash
   docker-compose up
   ```
   This builds (if not already built) and starts the container(s) defined in `docker-compose.yaml`, wiring up ports, environment variables, and networking automatically.

5. **Stop and clean up:**
   ```bash
   docker-compose down
   ```
   This stops the running containers and removes the containers/networks that `docker-compose up` created (the built image itself remains locally).

## Project Structure

```
Spring-Docker/
├── .mvn/wrapper/          # Maven wrapper files
├── src/                   # Application source code
├── Dockerfile             # Instructions to build the app's Docker image
├── docker-compose.yaml    # Multi-container run configuration
├── mvnw / mvnw.cmd        # Maven wrapper scripts (Linux/macOS / Windows)
├── pom.xml                # Maven project configuration & dependencies
└── README.md
```

## Prerequisites

- Java JDK 17+ (or whatever version the `pom.xml` targets)
- Maven (or use the included `mvnw` wrapper — no local Maven install required)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (includes Docker Engine, CLI, and Compose)
- A [Docker Hub](https://hub.docker.com/) account if you intend to `docker push` your own image
