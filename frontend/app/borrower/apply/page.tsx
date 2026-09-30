'use client';

import {useState} from 'react';
import {useRouter} from 'next/navigation';
import Link from 'next/link';
import api from '@/lib/api';

export default function GeneralLoanApplyPage(){
 const router=useRouter();const [amount,setAmount]=useState('');const [duration,setDuration]=useState('');const [purpose,setPurpose]=useState('');const [monthlyExpenses,setMonthlyExpenses]=useState('');const [existingMonthlyDebt,setExistingMonthlyDebt]=useState('');const [collateralDescription,setCollateralDescription]=useState('');const [collateralValue,setCollateralValue]=useState('');const [msg,setMsg]=useState('');const [busy,setBusy]=useState(false);
 const submit=async(e:any)=>{e.preventDefault();setBusy(true);setMsg('');try{await api.post('/general-loan-applications',{amount:Number(amount),duration:Number(duration),purpose,monthlyExpenses:Number(monthlyExpenses),existingMonthlyDebt:Number(existingMonthlyDebt),collateralDescription,collateralValue:Number(collateralValue||0),termsAccepted:false});setMsg('Ombi limetumwa. Mkopeshaji atakagua na ataweka offer yenye riba, ada na ratiba.');setAmount('');setDuration('');setPurpose('');setMonthlyExpenses('');setExistingMonthlyDebt('');setCollateralDescription('');setCollateralValue('');}catch(err:any){setMsg(err?.response?.data?.message||'Imeshindikana kutuma ombi.')}finally{setBusy(false)}};
 return <main className="min-h-screen bg-slate-50 p-6 dark:bg-slate-950"><div className="mx-auto max-w-2xl space-y-6">
  <section className="rounded-3xl bg-gradient-to-br from-blue-700 to-slate-900 p-6 text-white"><p className="text-sm text-blue-100">JmkLoanApp</p><h1 className="mt-1 text-3xl font-black">Omba Mkopo</h1><p className="mt-2 text-sm text-blue-100">Huhitaji kuona au kuchagua loan product. Tuma ombi la kiasi, muda na lengo; lender ataweka offer rasmi.</p></section>
  {msg&&<div className="rounded-2xl border bg-white p-4 text-sm dark:bg-slate-900">{msg}</div>}
  <form onSubmit={submit} className="space-y-4 rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
   <label className="block text-sm font-bold">Kiasi unachoomba (TZS)<input required min="1" type="number" value={amount} onChange={e=>setAmount(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label>
   <label className="block text-sm font-bold">Muda unaouomba (siku)<input required min="1" type="number" value={duration} onChange={e=>setDuration(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label>
   <div className="grid gap-4 sm:grid-cols-2"><label className="block text-sm font-bold">Gharama za mwezi (TZS)<input required min="0" type="number" value={monthlyExpenses} onChange={e=>setMonthlyExpenses(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label><label className="block text-sm font-bold">Madeni ya mwezi (TZS)<input required min="0" type="number" value={existingMonthlyDebt} onChange={e=>setExistingMonthlyDebt(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label></div><label className="block text-sm font-bold">Lengo la mkopo<textarea required maxLength={500} value={purpose} onChange={e=>setPurpose(e.target.value)} className="mt-2 min-h-28 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label>
   <button disabled={busy} className="w-full rounded-xl bg-blue-600 px-4 py-3 font-black text-white disabled:opacity-50">{busy?'Inatuma...':'Tuma ombi la mkopo'}</button>
   <Link href="/borrower" className="block text-center text-sm font-bold text-blue-600">Rudi Dashboard</Link>
  </form>
 </div></main>
}
