# JmkLoanApp — Tanzania lending operating flow

This workflow is designed for a licensed/approved lender. Software configuration does not itself grant a lending licence.

## 1. Institution setup
Admin configures legal entity, branches, staff, roles, approval limits, loan products, fees, penalties, notification providers, payment provider, complaint channels and compliance settings.

## 2. Customer onboarding
Borrower registers -> mobile/email verification -> profile -> identity/KYC documents -> KYC review -> approval -> customer becomes eligible to apply.

## 3. Loan application
Borrower selects an active product -> amount -> duration -> purpose -> confirms pricing/fees -> submits. System blocks duplicate active applications and validates product limits.

## 4. Credit assessment
Lender/credit officer opens the queue -> checks KYC -> income -> expenses -> existing debt -> repayment capacity -> prior loans -> risk signals -> records assessment. Automated scoring is decision support only; human approval remains required.

## 5. Review and approval
Application moves SUBMITTED -> UNDER_REVIEW -> APPROVED or REJECTED. Maker/checker separation should be used where required by the institution's policy. Approval must record actor, timestamp and reason.

## 6. Security/conditions before release
For products requiring security, guarantor/collateral conditions must be satisfied. Agreement is generated as PDF. Required parties sign electronically. System records signature metadata and audit trail.

## 7. Loan creation and release
Approved application becomes a loan. Repayment schedule is generated. The lender releases funds only after all configured pre-release conditions are satisfied. Provider callbacks/webhooks must be idempotent and reconciled.

## 8. Repayment
Customer pays through configured channels. Payment is stored with provider reference, amount, timestamp and status. Webhook confirmation updates the payment and allocates money against the repayment schedule.

## 9. Collections
Scheduler identifies overdue installments -> marks them overdue -> sends configured reminders -> creates collection tasks -> escalates according to policy -> supports promises to pay, restructuring and write-off workflows where authorized.

## 10. Reconciliation
Daily reconciliation compares provider transactions with internal payments, detects unmatched/duplicate/failed transactions, and requires authorized resolution with an audit event.

## 11. Complaints and consumer protection
Customer can submit a complaint, receive a reference number, see status, and receive a response/escalation path. Pricing, fees, key terms and a signed copy of the agreement must be accessible.

## 12. Reporting and audit
Admin receives portfolio, PAR/overdue, collections, repayments, disbursement, product, branch/user activity and audit reports. Sensitive actions are logged and access is role-limited.

## 13. Tanzania regulatory gate
Before production lending, the operator must confirm its applicable Bank of Tanzania licence/approval and compliance obligations. Bank of Tanzania states that lending/digital lending without the required licence/approval is prohibited, and its digital-lending guidance covers consumer protection, pricing, debt collection, personal-data protection and privacy. The system should therefore expose a compliance checklist rather than claiming regulatory approval.

## 14. Data protection
Collect only necessary personal data, document lawful processing/consent where applicable, enforce least-privilege access, protect secrets, encrypt sensitive traffic/storage as appropriate, maintain retention/deletion controls, and provide data-subject/complaint processes consistent with applicable Tanzanian data-protection requirements.

## Production acceptance checklist
- No demo credentials or fake provider success responses.
- Mobile-money provider credentials configured and tested in sandbox before production.
- SMS/email provider configured and delivery verified.
- KYC provider configured or manual KYC mode explicitly enabled.
- Backup and restore test completed.
- HTTPS and secure cookies/tokens verified.
- RBAC tested for every role.
- Audit trail tested for approvals, changes, signatures, payments and admin actions.
- Duplicate webhook/payment tests passed.
- Overdue scheduler and reminders tested.
- Financial calculations reviewed and tested against approved product rules.
- Regulatory/licensing review completed by the operating institution.
