'use client';

import { useEffect, useState } from 'react';
import { Download, FileText } from 'lucide-react';
import { agreementAPI, loanAPI } from '@/lib/api';

export default function AgreementsPage(){
 const [loans,setLoans]=useState<any[]>([]); const [msg,setMsg]=useState('');
 useEffect(()=>{(async()=>{try{const u=JSON.parse(localStorage.getItem('user')||'{}');const r=u.role==='LENDER'?await loanAPI.byLender():await loanAPI.byBorrower();setLoans(r.data?.data||r.data||[]);}catch(e){setMsg('Imeshindikana kupakia mikopo.')}})()},[]);
 const download=async(id:number)=>{try{const r=await agreementAPI.download(id);const url=URL.createObjectURL(r.data);const a=document.createElement('a');a.href=url;a.download='loan-'+id+'-agreement.pdf';a.click();URL.revokeObjectURL(url);}catch(e){setMsg('PDF haikupatikana au huna ruhusa.')}};
 return <main className="min-h-screen bg-slate-50 p-6 dark:bg-slate-950"><div className="mx-auto max-w-4xl space-y-6">
  <section className="rounded-3xl bg-white p-6 shadow-sm dark:bg-slate-900"><div className="flex items-center gap-3"><div className="rounded-xl bg-blue-50 p-3 text-blue-600"><FileText/></div><div><h1 className="text-2xl font-black">Loan Agreements</h1><p className="text-sm text-slate-500">Pakua mkataba wa kila mkopo kama PDF.</p></div></div></section>
  <section className="space-y-3">{loans.length===0?<div className="rounded-2xl bg-white p-8 text-center text-slate-500 dark:bg-slate-900">Hakuna mikopo.</div>:loans.map(l=><div key={l.id} className="flex items-center justify-between rounded-2xl bg-white p-5 shadow-sm dark:bg-slate-900"><div><b>Loan #{l.id}</b><p className="text-sm text-slate-500">TZS {l.amount} · {l.status}</p></div><button onClick={()=>download(l.id)} className="flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-bold text-white"><Download className="h-4 w-4"/> PDF</button></div>)}</section>{msg&&<div className="rounded-2xl bg-white p-4 text-sm dark:bg-slate-900">{msg}</div>}
 </div></main>;
}
