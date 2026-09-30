# JMK Loan Management & Financial Operations Platform — Architecture Specification

## Purpose
This is the implementation specification for a production-oriented, multi-organization lending and financial-operations platform for Tanzania. A feature is COMPLETE only when database, domain model, business logic, API, authorization, frontend, navigation, workflow, validation, audit, tests, deployment, and live verification are present.

## Core architecture
Next.js -> Spring Boot API -> domain/application services -> repositories -> PostgreSQL.

External integrations must use ports/interfaces and provider adapters:
Loan/Payment domain -> Integration interface -> Provider adapter -> external provider.

Asynchronous reliability must use transactional outbox/events for notifications, receipts, accounting events, webhooks and integrations.

## Core domains
- Identity, organizations, branches, users, roles and permissions
- Customer/KYC/document vault
- Guarantors and collateral
- Loan products and versioned terms
- Quotes and applications
- Credit assessment and approval workflow
- Agreements, signatures and document lifecycle
- Disbursement, repayments, allocation and receipts
- Collections, restructuring and write-off
- Double-entry accounting and reconciliation
- Notifications
- Complaints and compliance
- Risk/fraud/analytics
- Audit and security
- Configuration, feature flags and localization

## Non-negotiable invariants
1. Tenant scope is derived from authenticated identity; client-supplied organizationId/branchId cannot grant access.
2. Product changes never mutate historical loan/application terms.
3. Application quote, agreement, schedule and accounting use the same immutable terms snapshot/version.
4. Automated credit/risk scoring is decision support; authorized human approval remains the final decision.
5. Payment/webhook processing is authenticated, idempotent and auditable.
6. Accounting entries are balanced double-entry transactions.
7. Audit events are append-only/immutable and include actor, timestamp, action, entity, before/after where applicable, IP/device and correlation ID.
8. Guarantors see only information required for their guarantee exposure.
9. Sensitive KYC/documents are never exposed through generic entity serialization.
10. Provider failures must not silently lose business events; outbox/retry/reconciliation handles recovery.

## Configuration engine
Organization-scoped configuration must cover:
interest/fees/penalties, approval limits, notification rules, KYC requirements, branches, currency/timezone, localization, payment providers, feature flags, document/retention policy and compliance settings.

## Workflow engine
Approval workflows are data-driven and organization-scoped. A workflow may select approvers by role, branch, amount band, product and approval limit. Maker/checker separation must be enforceable.

## Loan lifecycle
APPLICATION -> REVIEW -> CREDIT_ASSESSMENT -> CONDITIONS -> APPROVAL/REJECTION -> AGREEMENT/SIGNATURES -> DISBURSEMENT -> REPAYMENT -> COLLECTION/RESTRUCTURE/WRITE_OFF -> CLOSED.

## Guarantor lifecycle
REQUESTED -> PENDING -> ACCEPTED/REJECTED -> ACTIVE -> RELEASED/CLOSED.

## Payment lifecycle
INITIATED -> PROVIDER_REQUEST -> PENDING -> AUTHENTICATED_WEBHOOK -> VERIFIED -> ALLOCATED -> RECEIPTED -> RECONCILED.
Duplicate provider references/events must be rejected or safely ignored.

## Accounting
Every financial event that affects balances must create balanced journal entries. Payment, disbursement, fee, penalty, reversal, write-off and adjustment must be traceable to source transactions.

## Privacy and data lifecycle
KYC/documents require purpose-aware access, consent where applicable, retention rules, secure storage, controlled download and lawful deletion/anonymization workflows.

## Localization
Organization settings control language, currency, timezone, date/number formatting and jurisdiction. Tanzania defaults: sw, TZS, Africa/Dar_es_Salaam.

## Operational dashboards
Dashboards must be permission-aware for CEO/admin, branch manager, credit, collections, cashier, accountant, compliance, auditor, borrower, lender and guarantor.

## Completion gate
Do not mark a module complete because an entity/controller exists. Verify:
DB -> Entity -> Service -> API -> Permissions -> Frontend -> Navigation -> Workflow -> Validation -> Audit -> Tests -> Deployment -> Live verification.

## Current implementation audit — 2026-09-30
- Organization/branch foundation: PARTIAL — entities/controllers exist, but tenant isolation and user-to-org/branch scope are not complete.
- RBAC: PARTIAL — coarse roles exist (ADMIN/LENDER/BORROWER/GUARANTOR); fine-grained permissions and approval limits are missing.
- Audit: PARTIAL — AuditService exists, but immutable before/after, IP/device and correlation-id event model is not complete.
- Loan products: PARTIAL — configurable product fields exist; organization scoping and immutable versioning are missing.
- Quotes/pre-agreement: PARTIAL — quote and pre-agreement endpoints exist; final agreement lifecycle must be verified end-to-end.
- Applications: PARTIAL — submission/review and snapshots exist; full workflow/duplicate checks/tenant scope need completion.
- Credit assessment: PARTIAL — module exists; maker/checker workflow and policy-driven scoring need completion.
- Signatures: PARTIAL — role-aware signing exists; complete agreement/document lifecycle and evidence requirements need completion.
- Guarantors: PARTIAL — backend module exists; restricted guarantor dashboard/exposure view needs completion.
- Repayment schedule: PARTIAL — UI/service exists; duration-unit correctness must be verified for DAYS vs MONTHS.
- Payments: PARTIAL — internal payment and allocation exist; production provider adapters are not real provider integrations yet.
- Webhooks: PARTIAL — authentication/idempotency foundation exists; provider-specific production contracts need completion.
- Collections: PARTIAL — cases/actions/scheduler foundation exists; full escalation/restructure/write-off workflow needs completion.
- Accounting: PARTIAL — account/journal foundations exist; automatic balanced postings across all financial events and reconciliation need completion.
- Notifications: PARTIAL — email/SMS hooks exist; provider configuration and reliable outbox delivery need completion.
- KYC/NIDA/CRB: PARTIAL — KYC foundation exists; legitimate production provider integrations are not complete.
- Reports: PARTIAL — admin reports exist; advanced portfolio/PAR/profitability/exports need completion.
- Complaints/compliance: PARTIAL — compliance settings exist; complete complaint workflow and regulatory reporting need completion.
- Backup/recovery/DR: MISSING from application architecture verification.
- Feature flags/config engine: MISSING as a centralized organization-scoped engine.
- Outbox/event reliability: MISSING as a first-class persisted mechanism.
- Full tenant isolation: MISSING.
- Full localization/jurisdiction engine: PARTIAL.
