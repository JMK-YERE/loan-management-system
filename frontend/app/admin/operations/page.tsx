'use client';

import { useEffect, useState } from 'react';
import { ClipboardList, Download, ShieldCheck, Settings } from 'lucide-react';
import { adminAPI, api } from '@/lib/api';

export default function OperationsPage(){
 const [audit,setAudit]=useState<any[]>([]);const [compliance,setCompliance]=useState<any>(null);const [msg,setMsg]=useState('');
 useEffect(()=>{(async()=>{try{const [a,c]=await Promise.all([api.get('/admin/audit'),api.get('/admin/compliance')]);setAudit(a.data?.data||a.data||[]);setCompliance(c.data?.data||c.data)}catch(e){setMsg('Huna ruhusa au data haijapatikana.')}})()},[]);
 const csv=(name:string)=>{const a=document.createElement('a');a.href=(process.env.NEXT_PUBLIC_API_URL||'https://jmkloanapp-backend.onrender.com/api')+'/admin/reports/'+name+'.csv';a.click()};
 return <main className="min-h-screen bg-slate-50 p-6 dark:bg-slate-950"><div className="mx-auto max-w-6xl space-y-6">
  <section className="rounded-3xl bg-white p-6 shadow-sm dark:bg-slate-900"><h1 className="text-2xl font-black">Admin Operations & Compliance</h1><p className="text-sm text-slate-500">Audit trail, reports, KYC and lending policy controls.</p></section>
  <section className="grid gap-4 md:grid-cols-3"><button onClick={()=>csv('loans')} className="rounded-2xl bg-white p-5 text-left shadow-sm dark:bg-slate-900"><Download className="mb-3 text-blue-600"/><b>Export Loans CSV</b><p className="text-sm text-slate-500">Advanced loan report.</p></button><button onClick={()=>csv('payments')} className="rounded-2xl bg-white p-5 text-left shadow-sm dark:bg-slate-900"><Download className="mb-3 text-emerald-600"/><b>Export Payments CSV</b><p className="text-sm text-slate-500">Reconciliation report.</p></button><a href="/admin/settings" className="rounded-2xl bg-white p-5 shadow-sm dark:bg-slate-900"><Settings className="mb-3 text-purple-600"/><b>System settings</b><p className="text-sm text-slate-500">Configure application controls.</p></a></section>
  <section className="grid gap-6 lg:grid-cols-2"><div className="rounded-3xl bg-white p-6 shadow-sm dark:bg-slate-900"><div className="flex items-center gap-2"><ClipboardList/><h2 className="font-black">Audit Trail</h2></div><div className="mt-4 max-h-96 space-y-2 overflow-auto">{audit.map(x=><div key={x.id} className="rounded-xl bg-slate-50 p-3 text-xs dark:bg-slate-950"><b>{x.action}</b> · {x.actorEmail||'system'} · {x.createdAt}<div className="text-slate-500">{x.details}</div></div>)}</div></div>
  <div className="rounded-3xl bg-white p-6 shadow-sm dark:bg-slate-900"><div className="flex items-center gap-2"><ShieldCheck/><h2 className="font-black">Compliance configuration</h2></div>{compliance&&<div className="mt-4 space-y-3 text-sm"><div className="flex justify-between"><span>Currency</span><b>{compliance.currency}</b></div><div className="flex justify-between"><span>Max loan</span><b>{compliance.maxLoanAmount||'Not set'}</b></div><div className="flex justify-between"><span>Max annual interest</span><b>{compliance.maxAnnualInterestPercent||'Not set'}</b></div><div className="rounded-xl bg-amber-50 p-3 text-amber-800">Set actual limits only after confirming your licensed business model and current Bank of Tanzania requirements.</div></div>}</div></section>{msg&&<div className="rounded-xl bg-white p-4 text-sm dark:bg-slate-900">{msg}</div>}
 </div></main>;
}
