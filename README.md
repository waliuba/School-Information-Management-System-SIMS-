# SIMS Project: Setup, Docker Compose, Progress and Troubleshooting Notes

> **Purpose:** This README records the project setup, the Docker Compose workflow we agreed to use, the work completed so far, and the current debugging position. SIMS is a learning and recruiter-portfolio project, not a production school-management service.

## 1. Project overview

SIMS (School Information Management System) is a full-stack application built with:

- **Frontend:** React, Vite, Axios
- **Backend:** Java, Spring Boot, Spring Security, JWT authentication
- **Database:** Oracle Database 21c Express Edition (XE)
- **Containers:** Docker and Docker Compose
- **Source control:** Git and GitHub

The project contains two application directories:

```text
sims_project/
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── front_end/
│   ├── Dockerfile
│   ├── .env.example
│   └── ...
├── compose.yaml
└── README.md
```

The exact files in the working tree may differ. Check the repository before relying on this illustrative tree.

## 2. What we are trying to achieve

The goal is a repeatable local setup that can be started through Docker Compose, rather than relying on separately launching the React and Spring Boot applications by hand.

This helps make the project easier to reproduce and explain to recruiters. It does **not** mean every code change automatically appears in a running container: source changes generally require rebuilding the relevant image unless a development bind mount is configured.

## 3. Agreed Docker Compose workflow

Run these commands from the **project root**, the directory containing `compose.yaml`.

### Normal startup

```bash
docker compose up -d
```

Starts the services using the images and configuration currently available.

### After code or configuration changes

```bash
docker compose up --build -d
```

Rebuilds images where required and starts the services. This is the normal command after editing frontend or backend code that is baked into the image.

### Check running services

```bash
docker compose ps
```

Shows service/container status and published ports.

### Follow service logs

```bash
docker compose logs -f
```

Follows logs from the Compose services. Press `Ctrl+C` to stop following logs; this does not normally stop the containers.

To inspect one service:

```bash
docker compose logs -f backend
docker compose logs -f frontend
```

Use the service names defined in the current `compose.yaml`. If they differ, substitute the actual names.

### Stop the project

```bash
docker compose down
```

Stops and removes the containers and Compose network. It does not normally delete named volumes, but this project’s actual volume configuration must be checked before relying on that.

### Useful additional checks

```bash
docker compose config
docker compose images
docker compose ps -a
docker ps
git status --short
```

- `docker compose config` validates and renders the effective Compose configuration. Treat its output as potentially sensitive if environment variables are expanded.
- `docker compose images` lists images associated with the Compose project.
- `docker compose ps -a` includes stopped Compose containers.
- `docker ps` lists currently running Docker containers.
- `git status --short` shows local file changes that may not have been committed.

## 4. Last confirmed local Docker state

The last recorded check on **20 October 2026** showed:

| Component | Last confirmed state |
|---|---|
| Compose frontend container | `sims_project-frontend-1` was running |
| Frontend host port | `5173` mapped to container port `80` |
| Compose backend container | `sims_project-backend-1` was running |
| Backend networking | Backend used host networking, so its port was not shown as a normal published-port mapping |
| Local Oracle database | Oracle XE ran separately on Windows, not as a local Compose container |
| Oracle listener | Windows Oracle listener was reachable at port `1521` |
| Backend datasource | `jdbc:oracle:thin:@localhost:1521/XEPDB1` |
| Database username | `SIMS_DB` |
| Current verification | Must be repeated in the current working tree and running environment |

This is a **historical snapshot**, not proof that those containers are still running today.

### Important networking note

The backend configuration previously used host networking and the datasource URL:

```text
jdbc:oracle:thin:@localhost:1521/XEPDB1
```

That setup was intended to let the backend container reach the Oracle listener on the host. Do not change `network_mode`, database hostnames, ports, or Compose service definitions just to troubleshoot a browser request until the current `compose.yaml` has been inspected.

Host networking behaves differently across operating systems and Docker environments. The current setup should be verified rather than assumed.

## 5. Frontend-to-backend request path

The backend login controller is mapped to:

```text
/api/auth/login
```

The frontend Axios client uses an environment-provided base URL:

```javascript
const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});
```

The login function posts to the `/auth/login` path:

```javascript
export async function login(credentials) {
  const response = await axiosClient.post("/auth/login", credentials);
  return response.data;
}
```

The intended local API base URL previously discussed was:

```dotenv
VITE_API_BASE_URL=http://localhost:8080/api
```

With that value, the final login URL should be:

```text
http://localhost:8080/api/auth/login
```

Check the current `front_end/.env` locally to confirm the actual value. Do not commit secrets or private environment files. Keep `.env.example` limited to safe placeholder values.

## 6. Login/CORS debugging history

The latest recorded browser debugging went through these stages:

1. A request went to `http://localhost:5174/auth/login` and returned `404 Not Found`. The request was missing the expected `/api` prefix and was going to the frontend's port.
2. The request URL was then corrected to `http://localhost:8080/api/auth/login`.
3. The browser sent a CORS preflight `OPTIONS` request to that URL, and the response was `403 Forbidden`.
4. The supplied Spring Security configuration enabled CORS, disabled CSRF, used stateless sessions, permitted `/api/auth/login`, and added a JWT filter before `UsernamePasswordAuthenticationFilter`.
5. The supplied CORS bean allowed `http://localhost:5174`, `http://127.0.0.1:5174`, and ports `3000`; it allowed `POST` and `OPTIONS`, and the `Content-Type` header.
6. The Compose frontend was last recorded on port `5173`, whereas the browser origin in the failing request was `http://localhost:5174`.

### What this tells us

- The `404` stage was consistent with a frontend/API base URL problem.
- The later `OPTIONS 403` is a separate stage. A browser sends preflight requests before certain cross-origin requests.
- The supplied CORS configuration appears to include the reported origin and request method/header. Therefore, do not immediately add duplicate CORS rules or randomly change allowed origins.
- A likely thing to verify first is **which frontend and backend processes are actually running** and whether the backend container contains the latest code/configuration.
- A custom JWT filter may also need to allow preflight requests through without trying to authenticate them. Inspect its implementation before changing it.
- A permitted `POST /api/auth/login` rule does not automatically prove the `OPTIONS` preflight is being handled correctly.

## 7. Check whether yesterday's work changed Docker or Compose

No current Git diff or live container inspection was available when this README was prepared. Use these checks to establish the facts before making changes.

From the repository root:

```bash
git status --short
git log --since="2026-10-20 00:00" --oneline
git diff -- compose.yaml backend/Dockerfile front_end/Dockerfile .dockerignore front_end/.env.example
docker compose ps -a
docker compose config
```

If you need to compare the tracked Compose file with the last commit:

```bash
git diff HEAD -- compose.yaml
git log -p -5 -- compose.yaml
```

If a command reports that a file does not exist, first confirm the actual filename and path. Do not create a replacement Compose file just because a command used the wrong path.

### How to interpret the results

- **No Compose/Dockerfile diff:** there may have been no tracked changes to those files, but untracked files and runtime state still need separate inspection.
- **Compose containers running:** Compose is running services now, but that does not guarantee they contain the newest source code.
- **Container image older than source changes:** rebuild with `docker compose up --build -d`.
- **Frontend on port 5173 and browser on 5174:** verify which page/tab or development server is being used. Do not assume both ports serve the same build.
- **Backend not responding on 8080:** inspect `docker compose ps -a` and backend logs before starting another backend process.
- **Unexpected environment changes:** inspect values privately. Do not paste passwords, JWT secrets, database credentials, or the full contents of `.env` into chat or commit them.

## 8. Oracle notes

The local database is Oracle Database 21c XE on Windows, with the `XEPDB1` service and the `SIMS_DB` schema.

The listener was previously confirmed ready on port `1521`. The `SIMS_DB` account had been found locked and was subsequently unlocked through SQL Developer. A previous local Maven startup failed with `ORA-28000` while the account was locked.

Do not reset the password or repeat the account-unlock process automatically. First check the current database state and the actual error in the backend logs. The local Windows Oracle instance and the Oracle container on AWS are separate environments; changes to one do not automatically change the other.

## 9. Local development and AWS are separate

There are two distinct environments:

- **Local:** WSL/Windows, local Docker Compose, frontend typically on `localhost:5173`, backend on port `8080`, Oracle XE running on Windows.
- **AWS demo:** a separate EC2 environment with its own containers and database setup.

Rebuilding local images does not deploy them to AWS. Do not run AWS commands or change the EC2 deployment when the task is to debug the local Compose setup.

Never commit `.env` files containing credentials, and never include passwords or tokens in this README.

## 10. Current project status and next steps

### Established from the latest recorded work

- React frontend and Spring Boot backend are containerized.
- Docker Compose is the agreed local run workflow.
- The frontend image was previously published on host port `5173`.
- The backend was previously configured to connect to local Oracle XE.
- The login endpoint and frontend request path have been identified.
- The last browser issue recorded was a CORS preflight `OPTIONS 403` when the page origin was `http://localhost:5174`.

### Still to verify

1. Run `docker compose ps -a` from the project root to see the current service state.
2. Review `git status --short` and the Git diff/log commands above to see whether yesterday changed Docker or Compose files.
3. Inspect the current `compose.yaml` and confirm service names, networking, ports, environment sources, and any volume mounts.
4. Confirm which frontend is open: the Compose frontend on port `5173` or a separate Vite server on port `5174`.
5. Inspect the backend logs and the custom JWT filter to find why the preflight is receiving `403`.
6. Once code/configuration changes are made, rebuild through Compose and retest the same request.

### Next diagnostic commands

Run these one at a time from the project root and keep the output available:

```bash
docker compose ps -a
git status --short
git diff HEAD -- compose.yaml backend/Dockerfile front_end/Dockerfile .dockerignore front_end/.env.example
docker compose logs --tail=100 backend
```

If the backend service is not named `backend`, use the service name shown by `docker compose config --services`. Review logs for credentials before sharing them.

## 11. Project direction

The foundation is in place. The next stage is about connecting the React frontend to the Spring Boot APIs, completing authentication and attendance, strengthening testing, and preparing SIMS for containerized deployment and CI/CD.

Roadmap:

- **Completed:** Core management modules
- **In progress:** Frontend integration
- **Next:** Authentication and roles
- **Planned:** Attendance, reports, analytics, CI/CD

The README should be updated when a meaningful setup, architecture, or deployment decision changes.
