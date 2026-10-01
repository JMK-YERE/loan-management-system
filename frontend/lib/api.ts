import axios from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'https://jmkloanapp-backend.onrender.com/api';

export const api = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

api.interceptors.request.use((config) => {
  if (typeof window !== 'undefined') {
    const token = localStorage.getItem('token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && typeof window !== 'undefined') {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      document.cookie = 'token=; path=/; max-age=0';
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  register: (data: any) => api.post('/auth/register', data),
  login: (data: any) => api.post('/auth/login', data),
  google: (credential: string) => api.post('/auth/google', { credential }),
  setPassword: (data: any) => api.post('/auth/set-password', data),
  forgotPassword: (data: any) => api.post('/auth/forgot-password', data),
  resetPassword: (data: any) => api.post('/auth/reset-password', data),
  changePassword: (data: any) => api.put('/auth/change-password', data),
  health: () => api.get('/auth/health'),
};

export const announcementAPI = {
  getPublic: () => api.get('/announcements/public'),
  getAll: () => api.get('/announcements'),
  getById: (id: number) => api.get(`/announcements/${id}`),
  create: (data: any) => api.post('/announcements', data),
  update: (id: number, data: any) => api.put(`/announcements/${id}`, data),
  delete: (id: number) => api.delete(`/announcements/${id}`),
};

export const systemAPI = {
  status: () => api.get('/admin/system/status'),
};

export const adminUserAPI = {
  list: () => api.get('/admin/users'),
  changeRole: (id: number, role: string) => api.patch('/admin/users/' + id + '/role', { role }),
  setActive: (id: number, active: boolean) => api.patch('/admin/users/' + id + '/active', { active }),
};

export const adminAPI = {
  stats: () => api.get('/admin/stats'),
  list: (status?: string) => api.get('/admin/applicants', { params: status ? { status } : {} }),
  get: (id: number) => api.get(`/admin/applicants/${id}`),
  approve: (id: number) => api.post(`/admin/applicants/${id}/approve`),
  reject: (id: number, reason: string) => api.post(`/admin/applicants/${id}/reject`, { reason }),
};

export const userAPI = {
  me: () => api.get('/me'),
  borrowers: () => api.get('/users/borrowers'),
  guarantors: () => api.get('/users/guarantors'),
};

export const generalLoanApplicationAPI = {
  submit: (data:any) => api.post('/general-loan-applications', data),
  mine: () => api.get('/general-loan-applications/mine'),
  pending: () => api.get('/general-loan-applications/pending'),
  review: (id:number) => api.put('/general-loan-applications/'+id+'/review'),
  assignProduct: (id:number, productId:number) => api.put('/general-loan-applications/'+id+'/assign-product/'+productId),
  acceptOffer: (id:number) => api.put('/general-loan-applications/'+id+'/accept-offer'),
  approve: (id:number) => api.put('/general-loan-applications/'+id+'/approve'),
};

export const loanApplicationAPI = {
  submit: (data:any) => api.post('/loan-applications', data),
  mine: () => api.get('/loan-applications/mine'),
  pending: () => api.get('/loan-applications/pending'),
  review: (id:number) => api.put('/loan-applications/'+id+'/review'),
  approve: (id:number) => api.put('/loan-applications/'+id+'/approve'),
  reject: (id:number, reason?:string) => api.put('/loan-applications/'+id+'/reject', {reason}),
};

export const repaymentAPI = { schedule: (loanId: number) => api.get('/loans/' + loanId + '/schedule'), };

export const loanAPI = {
  byLender: () => api.get('/loans/lender'),
  byBorrower: () => api.get('/loans/borrower'),
  get: (id: number) => api.get(`/loans/${id}`),
  create: (borrowerId: number, data: any) => api.post('/loans', data, { params: { borrowerId } }),
  approve: (id: number) => api.put(`/loans/${id}/approve`),
  disburse: (id: number) => api.put(`/loans/${id}/disburse`),
  reject: (id: number) => api.put(`/loans/${id}/reject`),
};

export const paymentAPI = {
  create: (data: any) => api.post('/payments', data),
  byLoan: (loanId: number) => api.get(`/payments/loan/${loanId}`),
  get: (id: number) => api.get(`/payments/${id}`),
  confirm: (id: number, transactionId: string) =>
    api.put(`/payments/${id}/confirm`, null, { params: { transactionId } }),
};

export const guarantorAPI = {
  byLoan: (loanId: number) => api.get(`/guarantors/loan/${loanId}`),
  mine: () => api.get('/guarantors/mine'),
  add: (loanId: number, data: any) => api.post(`/guarantors/loan/${loanId}`, data),
  approve: (id: number) => api.put(`/guarantors/${id}/approve`),
  reject: (id: number) => api.put(`/guarantors/${id}/reject`),
};

export const signatureAPI = {
  create: (data: any) => api.post('/signatures', data),
  byLoan: (loanId: number) => api.get(`/signatures/loan/${loanId}`),
};

export default api;

export const agreementAPI = {
  preview: (loanId: number) => api.get('/loan-agreements/' + loanId + '/preview', { responseType: 'blob' }),
  download: (loanId: number) => api.get('/loan-agreements/' + loanId + '/pdf', { responseType: 'blob' }),
  applicationPreview: (applicationId: number) => api.get('/agreements/application/' + applicationId + '/preview', { responseType: 'blob' }),
  applicationDownload: (applicationId: number) => api.get('/agreements/application/' + applicationId + '/pdf', { responseType: 'blob' }),
};

export const bursarAPI = {
  summary: () => api.get('/bursar/summary'),
  loans: () => api.get('/bursar/loans'),
  payments: () => api.get('/bursar/payments'),
  report: () => api.get('/bursar/report.csv', { responseType: 'blob' }),
};

export const mobileMoneyAPI = {
  checkout: (paymentId: number, phone: string) => api.post('/mobile-money/checkout/' + paymentId, { phone }),
};


export const loanProductAPI = {
  active: () => api.get('/loan-products'),
  all: () => api.get('/loan-products/all'),
  create: (data: any) => api.post('/loan-products', data),
  setActive: (id: number, value: boolean) => api.patch('/loan-products/' + id + '/active', null, { params: { value } }),
};


export const creditAssessmentAPI = {
  assess: (applicationId: number, data: { monthlyExpenses: number; existingMonthlyDebt: number }) => api.post('/credit-assessments/' + applicationId, data),
  get: (applicationId: number) => api.get('/credit-assessments/' + applicationId),
};


export const loanQuoteAPI = {
  quote: (data:any) => api.post('/loan-quotes', data),
};

export const preAgreementAPI = {
  pdf: (data:any, purpose:string) => api.post('/pre-agreements/pdf', data, { params: { purpose }, responseType: 'blob' }),
};
