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
  setPassword: (data: any) => api.post('/auth/set-password', data),
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

export const adminAPI = {
  list: (status?: string) => api.get('/admin/applicants', { params: status ? { status } : {} }),
  get: (id: number) => api.get(`/admin/applicants/${id}`),
  approve: (id: number) => api.post(`/admin/applicants/${id}/approve`),
  reject: (id: number, reason: string) => api.post(`/admin/applicants/${id}/reject`, { reason }),
};

export const userAPI = {
  me: () => api.get('/me'),
};

export const loanAPI = {
  byLender: () => api.get('/loans/lender'),
  byBorrower: () => api.get('/loans/borrower'),
  get: (id: number) => api.get(`/loans/${id}`),
  create: (borrowerId: number, data: any) => api.post('/loans', data, { params: { borrowerId } }),
  approve: (id: number) => api.put(`/loans/${id}/approve`),
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
