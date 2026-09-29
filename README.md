# JMK Loan Management System

A full-stack loan management platform built with **Next.js**, **Spring Boot**, **PostgreSQL**, and **JWT authentication**.

## Core features

- Role-based access for Admin, Lender, Borrower, and Guarantor
- Loan creation, approval, rejection, and status tracking
- Loan calculator with interest, processing fee, lawyer fee, total repayment, and monthly installment
- Borrower loan/payment history
- Payment recording and confirmation
- Guarantor management
- Digital signature support
- Applicant approval workflow
- Admin announcements and user management
- Responsive dashboard with search/filtering
- Render deployment configuration

## Architecture

- `frontend/` — Next.js web application
- `backend/` — Spring Boot REST API
- PostgreSQL — persistent application database
- Render — production hosting

## Production API

The frontend is configured to use the Render backend through `NEXT_PUBLIC_API_URL`.

## Development

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Deployment

The repository includes `render.yaml` for the frontend and backend services. Changes pushed to the default branch can trigger the configured Render deployments.
