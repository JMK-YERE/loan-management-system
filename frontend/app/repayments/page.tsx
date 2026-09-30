'use client';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { repaymentAPI, loanAPI } from '@/lib/api';
const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));
export default function RepaymentsPage(){
 const router=useRouter(); const [loanId,setLoanId]=useState('');
 const [schedule,setSchedule]=useState<any[]>([]),[loan,setLoan]=useState<any>(null),[error,setError]=useState(''),[loading,setLoading]=useState(true);
 useEffect(()=>{const q=new URLSearchParams(window.location.search).get('loanId');if(q)setLoanId(q);const token=localStorage.getItem('token');if(!token){router.push('/login');return;}if(!loanId){setError('Loan haijachaguliwa.');setLoading(false);return;}(async()=>{try{const [s,l]=await Promise.all([repaymentAPI.schedule(Number(loanId)),loanAPI.get(Number(loanId))]);setSchedule(s.data?.data??s.data??[]);setLoan(l.data?.data??l.data);}catch(e:any){setError(e?.response?.data?.message||'Imeshindikana kupakia ratiba.')}finally{setLoading(false)}})()},[router,loanId]);
 const totalDue=schedule.reduce((n,x)=>n+Number(x.amountDue||0),0),totalPaid=schedule.reduce((n,x)=>n+Number(x.amountPaid||0),0);
 return <main className="min-h-screen bg-slate-50 p-4 dark:bg-slate-950 sm:p-6"><div className="mx-auto max-w-5xl space-y-5">
 <div className="flex flex-wrap items-center justify-between gap-3"><div><h1 className="text-2xl font-black dark:text-white">Ratiba ya Marejesho</h1><p className="text-sm text-slate-500">Loan #{loanId}{loan?.status?' · '+loan.status:''}</p></div><Link href="/borrower" className="rounded-xl border bg-white px-4 py-2 text-sm font-bold dark:border-slate-800 dark:bg-slate-900 dark:text-white">← Dashboard</Link></div>
 {error&&<div className="rounded-2xl border border-red-200 bg-red-50 p-4 text-sm text-red-700">{error}</div>}
 {loading?<div className="rounded-2xl bg-white p-8 text-center dark:bg-slate-900">Inapakia...</div>:<><div className="grid gap-3 sm:grid-cols-3">
 <div className="rounded-2xl bg-white p-5 shadow-sm dark:bg-slate-900"><span className="text-xs text-slate-500">Jumla ya ratiba</span><b className="mt-1 block text-xl dark:text-white">{money(totalDue)}</b></div>
 <div className="rounded-2xl bg-white p-5 shadow-sm dark:bg-slate-900"><span className="text-xs text-slate-500">Imelipwa</span><b className="mt-1 block text-xl dark:text-white">{money(totalPaid)}</b></div>
 <div className="rounded-2xl bg-white p-5 shadow-sm dark:bg-slate-900"><span className="text-xs text-slate-500">Bado</span><b className="mt-1 block text-xl dark:text-white">{money(Math.max(0,totalDue-totalPaid))}</b></div>
 </div><div className="overflow-x-auto rounded-2xl bg-white shadow-sm dark:bg-slate-900"><table className="w-full text-sm"><thead><tr className="border-b text-left dark:border-slate-800"><th className="p-4">#</th><th>Tarehe</th><th>Inayodaiwa</th><th>Imelipwa</th><th>Status</th></tr></thead><tbody>
 {schedule.map(s=><tr key={s.id} className="border-b dark:border-slate-800"><td className="p-4 font-bold">{s.installmentNumber}</td><td>{s.dueDate}</td><td>{money(s.amountDue)}</td><td>{money(s.amountPaid)}</td><td><span className="rounded-full bg-slate-100 px-2 py-1 text-xs font-bold dark:bg-slate-800">{s.status}</span></td></tr>)}
 {schedule.length===0&&<tr><td colSpan={5} className="p-8 text-center text-slate-500">Ratiba bado haijatengenezwa.</td></tr>}
 </tbody></table></div></>}</div></main>;
}