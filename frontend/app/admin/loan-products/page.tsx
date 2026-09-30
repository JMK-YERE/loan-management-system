'use client';

import { FormEvent, useEffect, useState } from 'react';
import { loanProductAPI } from '@/lib/api';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));

export default function LoanProductsPage(){
 const [items,setItems]=useState<any[]>([]);const [msg,setMsg]=useState('');const [busy,setBusy]=useState(false);
 const [form,setForm]=useState<any>({name:'',loanType:'QUICK',minAmount:20000,maxAmount:500000,minDuration:7,maxDuration:30,durationUnit:'DAYS',interestRate:5,interestType:'FLAT',processingFee:0,lateFee:0,gracePeriodDays:0,repaymentFrequency:'ONE_TIME'});
 const load=async()=>{try{const r=await loanProductAPI.all();setItems(r.data?.data||r.data||[])}catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kupakia products.')}};
 useEffect(()=>{load()},[]);
 const submit=async(e:FormEvent)=>{e.preventDefault();setBusy(true);setMsg('');try{await loanProductAPI.create({...form,minAmount:Number(form.minAmount),maxAmount:Number(form.maxAmount),minDuration:Number(form.minDuration),maxDuration:Number(form.maxDuration),interestRate:Number(form.interestRate),processingFee:Number(form.processingFee),lateFee:Number(form.lateFee),gracePeriodDays:Number(form.gracePeriodDays)});setMsg('Loan product imeundwa.');await load()}catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kuunda product.')}finally{setBusy(false)}};
 const set=(k:string,v:any)=>setForm((x:any)=>({...x,[k]:v}));
 return <main className="space-y-6 p-6"><div><h1 className="text-2xl font-black">Loan Products</h1><p className="text-sm text-slate-500">Sanidi Quick Loan na Monthly/Installment Loan bila kubadilisha code.</p></div>
 {msg&&<div className="rounded-xl bg-blue-50 p-3 text-sm text-blue-700">{msg}</div>}
 <form onSubmit={submit} className="grid gap-4 rounded-2xl bg-white p-6 shadow-sm md:grid-cols-3 dark:bg-slate-900">
  <input required placeholder="Product name" value={form.name} onChange={e=>set('name',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"/>
  <select value={form.loanType} onChange={e=>set('loanType',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"><option value="QUICK">Quick Loan</option><option value="INSTALLMENT">Installment / Monthly</option></select>
  <select value={form.durationUnit} onChange={e=>set('durationUnit',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"><option value="DAYS">Days</option><option value="MONTHS">Months</option></select>
  <input type="number" required placeholder="Min amount" value={form.minAmount} onChange={e=>set('minAmount',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"/>
  <input type="number" required placeholder="Max amount" value={form.maxAmount} onChange={e=>set('maxAmount',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"/>
  <input type="number" required placeholder="Min duration" value={form.minDuration} onChange={e=>set('minDuration',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"/>
  <input type="number" required placeholder="Max duration" value={form.maxDuration} onChange={e=>set('maxDuration',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"/>
  <input type="number" step="0.01" required placeholder="Interest rate %" value={form.interestRate} onChange={e=>set('interestRate',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"/>
  <select value={form.interestType} onChange={e=>set('interestType',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"><option value="FLAT">Flat</option><option value="ANNUAL_SIMPLE">Annual Simple</option></select>
  <select value={form.repaymentFrequency} onChange={e=>set('repaymentFrequency',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"><option value="ONE_TIME">One Time</option><option value="DAILY">Daily</option><option value="WEEKLY">Weekly</option><option value="MONTHLY">Monthly</option></select>
  <input type="number" placeholder="Processing fee" value={form.processingFee} onChange={e=>set('processingFee',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"/>
  <input type="number" placeholder="Late fee" value={form.lateFee} onChange={e=>set('lateFee',e.target.value)} className="rounded-xl border p-3 dark:bg-slate-950"/>
  <button disabled={busy} className="rounded-xl bg-blue-600 px-4 py-3 font-bold text-white">{busy?'Inahifadhi...':'Create Product'}</button>
 </form>
 <div className="overflow-x-auto rounded-2xl bg-white shadow-sm dark:bg-slate-900"><table className="w-full text-sm"><thead><tr className="border-b text-left"><th className="p-4">Product</th><th>Type</th><th>Range</th><th>Duration</th><th>Rate</th><th>Status</th></tr></thead><tbody>{items.map(p=><tr key={p.id} className="border-b"><td className="p-4 font-bold">{p.name}</td><td>{p.loanType}</td><td>{money(p.minAmount)} – {money(p.maxAmount)}</td><td>{p.minDuration}–{p.maxDuration} {p.durationUnit.toLowerCase()}</td><td>{p.interestRate}%</td><td><button onClick={async()=>{await loanProductAPI.setActive(p.id,!p.active);load()}} className="font-bold">{p.active?'ACTIVE':'INACTIVE'}</button></td></tr>)}</tbody></table></div>
 </main>;
}