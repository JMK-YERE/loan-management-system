'use client';

import {useEffect,useState} from 'react';
import {useRouter} from 'next/navigation';
import Link from 'next/link';
import {CheckCircle2,Clock3,FileSignature,FileText,LogOut,RefreshCw,ShieldCheck} from 'lucide-react';
import {generalLoanApplicationAPI,loanAPI} from '@/lib/api';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import {useLanguage} from '@/lib/useLanguage';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));
const unwrap=(r:any)=>r?.data?.data??r?.data??[];

export default function LenderPage(){
 const router=useRouter();const {lang}=useLanguage();const en=lang==='en';
 const [user,setUser]=useState<any>(null),[apps,setApps]=useState<any[]>([]),[loans,setLoans]=useState<any[]>([]);
 const [selected,setSelected]=useState<Record<number,string>>({}),[busy,setBusy]=useState(false),[message,setMessage]=useState('');

 const load=async()=>{
  try{
   const [a,l]=await Promise.all([generalLoanApplicationAPI.pending(),loanAPI.byLender()]);
   setApps(unwrap(a));setLoans(unwrap(l));
  }catch(e:any){setMessage(e?.response?.data?.message||(en?'Failed to load lender workspace.':'Imeshindikana kupakia workspace ya mkopeshaji.'))}
 };

 useEffect(()=>{
  const token=localStorage.getItem('token'),raw=localStorage.getItem('user');
  if(!token||!raw){router.push('/login');return}
  const u=JSON.parse(raw);
  if(u.role!=='LENDER'&&u.role!=='ADMIN'){router.push('/dashboard');return}
  setUser(u);load();
 },[router]);

 const action=async(fn:()=>Promise<any>,ok:string)=>{
  setBusy(true);setMessage('');
  try{await fn();setMessage(ok);await load();}
  catch(e:any){setMessage(e?.response?.data?.message||(en?'Action failed.':'Kitendo kimeshindikana.'))}
  finally{setBusy(false)}
 };

 const pendingApps=apps.filter(a=>a.status==='SUBMITTED').length;
 const offers=apps.filter(a=>a.status==='OFFER_READY').length;
 const pendingLoans=apps.filter(a=>a.status==='OFFER_ACCEPTED').length;
 const approvedLoans=loans.filter(l=>l.status==='APPROVED').length;

 const logout=()=>{localStorage.clear();document.cookie='token=; path=/; max-age=0';router.push('/login')};
 if(!user)return null;

 return <div className="min-h-screen bg-slate-50 text-slate-900 dark:bg-slate-950 dark:text-white">
  <nav className="sticky top-0 z-40 border-b bg-white/95 px-4 py-3 backdrop-blur dark:border-slate-800 dark:bg-slate-900/95">
   <div className="mx-auto flex max-w-7xl items-center justify-between">
    <Link href="/" className="font-black">💰 JmkLoanApp · Lender</Link>
    <div className="flex items-center gap-2"><LanguageSwitcher/><Link href="/profile" className="rounded-xl px-3 py-2 text-sm font-semibold">Profile</Link><button onClick={logout} className="rounded-xl px-3 py-2 text-sm font-semibold text-red-600"><LogOut className="mr-1 inline h-4 w-4"/>{en?'Logout':'Toka'}</button></div>
   </div>
  </nav>

  <main className="mx-auto max-w-7xl space-y-6 p-4 sm:p-6">
   <section className="rounded-3xl bg-gradient-to-br from-indigo-700 via-blue-700 to-slate-950 p-7 text-white shadow-xl">
    <p className="text-sm text-blue-100">{en?'Lender workspace':'Workspace ya Mkopeshaji'}</p>
    <h1 className="mt-1 text-3xl font-black">{en?'Loan workflow control center':'Kituo cha kusimamia mchakato wa mikopo'}</h1>
    <p className="mt-3 max-w-3xl text-sm leading-6 text-blue-100">{en?'Review applications, prepare borrower offers, approve converted loans, manage guarantors, signatures and disbursement.':'Kagua maombi, andaa offer kwa mkopaji, idhinisha loan iliyokubaliwa, simamia wadhamini, sahihi na utoaji wa mkopo.'}</p>
   </section>

   {message&&<div className="rounded-2xl border border-blue-200 bg-blue-50 p-4 text-sm font-semibold text-blue-800 dark:border-blue-900 dark:bg-blue-950/30 dark:text-blue-200">{message}</div>}

   <div className="flex flex-wrap gap-2">
    <Link href="/lender/general-applications" className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-black text-white">📥 General Loan Requests</Link>
    <Link href="/agreements" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:border-slate-800 dark:bg-slate-900">📄 Agreements</Link>
    <Link href="/signatures" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:border-slate-800 dark:bg-slate-900">✍ E-Signatures</Link>
    <Link href="/repayments" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:border-slate-800 dark:bg-slate-900">📅 Repayments</Link>
   </div>

   <section className="grid gap-4 sm:grid-cols-4">
    {[
     ['New requests',pendingApps,Clock3,'SUBMITTED'],
     ['Offers awaiting acceptance',offers,FileText,'OFFER'],
     ['Loans awaiting final approval',pendingLoans,CheckCircle2,'PENDING'],
     ['Approved loans',approvedLoans,ShieldCheck,'APPROVED']
    ].map(([title,value,Icon,key]:any)=><div key={key} className="rounded-2xl border bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-slate-900"><Icon className="h-5 w-5 text-blue-600"/><div className="mt-2 text-3xl font-black">{value}</div><div className="text-sm text-slate-500">{en?title:title==='New requests'?'Maombi mapya':title==='Offers awaiting acceptance'?'Offers zinazosubiri acceptance':title==='Loans awaiting final approval'?'Loans zinazosubiri approval':'Loans zilizoidhinishwa'}</div></div>)}
   </section>

   <section className="rounded-3xl border bg-white shadow-sm dark:border-slate-800 dark:bg-slate-900">
    <div className="flex flex-wrap items-center justify-between gap-3 border-b p-5 dark:border-slate-800">
     <div><h2 className="text-xl font-black">Applications zinazohitaji action</h2><p className="text-sm text-slate-500">Kila request inaonekana hapa na action yake inayofuata.</p></div>
     <Link href="/lender/general-applications" className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-black text-white">Fungua Queue</Link>
    </div>
    <div className="space-y-3 p-5">
     {apps.length===0?<div className="rounded-2xl bg-slate-50 p-6 text-center text-sm text-slate-500 dark:bg-slate-950">Hakuna application inayosubiri.</div>:apps.slice(0,8).map(a=><div key={a.id} className="rounded-2xl border p-4 dark:border-slate-800">
      <div className="flex flex-wrap items-start justify-between gap-3"><div><b>Application #{a.id}</b><p className="text-sm text-slate-500">{a.borrower?.fullName||'Mkopaji'} · {money(a.amount)} · {a.duration} {a.durationUnit||'DAYS'}</p></div><span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-black dark:bg-slate-800">{a.status}</span></div>
      <div className="mt-3 flex flex-wrap gap-2">
       {a.status==='SUBMITTED'&&<button disabled={busy} onClick={()=>action(()=>generalLoanApplicationAPI.review(a.id),'Application imehamia UNDER_REVIEW.')} className="rounded-xl bg-blue-600 px-4 py-2 text-xs font-black text-white">Anza Review</button>}
       {a.status==='UNDER_REVIEW'&&<Link href="/lender/general-applications" className="rounded-xl border px-4 py-2 text-xs font-black">Chagua Product / Tengeneza Offer</Link>}
       {a.status==='OFFER_READY'&&<Link href="/lender/general-applications" className="rounded-xl border border-amber-300 px-4 py-2 text-xs font-black text-amber-700">Subiri Borrower Accept</Link>}
       {a.status==='OFFER_ACCEPTED'&&<Link href="/lender/general-applications" className="rounded-xl bg-emerald-600 px-4 py-2 text-xs font-black text-white">Final Approval</Link>}
      </div>
     </div>)}
    </div>
   </section>

   <section className="rounded-3xl border bg-white shadow-sm dark:border-slate-800 dark:bg-slate-900">
    <div className="flex flex-wrap items-center justify-between gap-3 border-b p-5 dark:border-slate-800">
     <div><h2 className="text-xl font-black">{en?'Application workflow':'Mtiririko wa maombi'}</h2><p className="text-sm text-slate-500">Application → Review → Offer → Borrower accepts → Create loan → Final approval → Signatures → Disbursement</p></div>
     <button onClick={load} className="rounded-xl border p-2"><RefreshCw className="h-4 w-4"/></button>
    </div>
    <div className="grid gap-3 p-5 md:grid-cols-5">
     {['1. Review request','2. Prepare offer','3. Borrower accepts','4. Create & approve loan','5. Sign & disburse'].map((x,i)=><div key={x} className="rounded-2xl border p-4 dark:border-slate-800"><div className="text-xs font-black text-blue-600">STEP {i+1}</div><div className="mt-2 text-sm font-bold">{en?x:['1. Kagua ombi','2. Andaa offer','3. Mkopaji akubali','4. Tengeneza na idhinisha loan','5. Saini na toa mkopo'][i]}</div></div>)}
    </div>
    <div className="p-5 pt-0"><Link href="/lender/general-applications" className="inline-flex rounded-xl bg-blue-600 px-5 py-3 text-sm font-black text-white">Fungua General Loan Requests →</Link></div>
   </section>

   <section className="rounded-3xl border bg-white shadow-sm dark:border-slate-800 dark:bg-slate-900">
    <div className="border-b p-5 dark:border-slate-800"><h2 className="text-xl font-black">Post-approval workflow</h2><p className="text-sm text-slate-500">Hapa lender anaendelea na guarantor, final approval, signatures na disbursement baada ya loan kutengenezwa.</p></div>
    {loans.filter(l=>['PENDING','APPROVED'].includes(l.status)).length===0?<div className="p-8 text-center text-sm text-slate-500">Hakuna loan inayosubiri hatua hizi.</div>:
    <div className="grid gap-4 p-5 md:grid-cols-2">{loans.filter(l=>['PENDING','APPROVED'].includes(l.status)).map(l=><div key={l.id} className="rounded-2xl border p-5 dark:border-slate-800">
     <div className="flex justify-between gap-3"><div><b>Loan #{l.id}</b><p className="text-sm text-slate-500">{l.borrower?.fullName} · {money(l.amount)}</p></div><span className="text-xs font-black">{l.status}</span></div>
     {l.status==='PENDING'&&<div className="mt-4 space-y-3">
      
      <button disabled={busy} onClick={()=>action(()=>loanAPI.approve(l.id),'Approval ya mwisho imekamilika.')} className="w-full rounded-xl border border-emerald-300 px-3 py-2 text-xs font-bold text-emerald-700">Final Approve Loan</button>
      <Link href={'/lender/onsite-guarantor?loanId='+l.id} className="block w-full rounded-xl border border-amber-300 px-3 py-2 text-center text-xs font-bold text-amber-700">📸 Mdhamini yupo hapa — Capture photo + signature</Link><Link href={'/lender/collateral?loanId='+l.id} className="block w-full rounded-xl border border-purple-300 px-3 py-2 text-center text-xs font-bold text-purple-700">📷 Dhamana — picha nyingi + verification</Link>
     </div>}
     {l.status==='APPROVED'&&<div className="mt-4 flex flex-wrap gap-2">
      <Link href={'/agreements?loanId='+l.id} className="rounded-xl border px-3 py-2 text-xs font-bold">Agreement</Link>
      <Link href={'/signatures?loanId='+l.id} className="rounded-xl border px-3 py-2 text-xs font-bold">Signatures</Link>
      <Link href={'/lender/onsite-guarantor?loanId='+l.id} className="rounded-xl border border-amber-300 px-3 py-2 text-xs font-bold text-amber-700">📸 Mdhamini onsite</Link>
      <button disabled={busy} onClick={()=>action(()=>loanAPI.disburse(l.id),'Mkopo umetolewa.')} className="rounded-xl bg-indigo-600 px-3 py-2 text-xs font-bold text-white">Disburse Loan</button>
     </div>}
    </div>)}</div>}
   </section>
  </main>
 </div>
}
