'use client';

import {useEffect,useMemo,useState} from 'react';
import Link from 'next/link';
import {useRouter} from 'next/navigation';
import {CheckCircle2,Clock3,CreditCard,Download,FileSignature,FileText,LogOut,RefreshCw,ShieldCheck,WalletCards} from 'lucide-react';
import {generalLoanApplicationAPI,loanAPI,repaymentAPI} from '@/lib/api';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import {useLanguage} from '@/lib/useLanguage';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));
const unwrap=(r:any)=>r?.data?.data??r?.data??[];
const statusLabel:any={SUBMITTED:'Lender anasubiri kuanza review',UNDER_REVIEW:'Offer / review inaendelea',CONVERTED:'Loan imetengenezwa',REJECTED:'Ombi limekataliwa',CANCELLED:'Ombi limefungwa',PENDING:'Loan inasubiri approval',APPROVED:'Imeidhinishwa',DISBURSED:'Imetolewa',ACTIVE:'Inaendelea',DEFAULTED:'Imechelewa',PAID:'Imelipwa'};

export default function BorrowerPage(){
 const router=useRouter();const {lang}=useLanguage();const [user,setUser]=useState<any>(null);
 const [applications,setApplications]=useState<any[]>([]),[loans,setLoans]=useState<any[]>([]),[schedules,setSchedules]=useState<Record<number,any[]>>({});
 const [loading,setLoading]=useState(true),[msg,setMsg]=useState(''),[amount,setAmount]=useState(''),[duration,setDuration]=useState(''),[purpose,setPurpose]=useState(''),[submitting,setSubmitting]=useState(false);

 const load=async()=>{
  setLoading(true);setMsg('');
  try{const [a,l]=await Promise.all([generalLoanApplicationAPI.mine(),loanAPI.byBorrower()]);
   const loanList=Array.isArray(unwrap(l))?unwrap(l):[]; setApplications(Array.isArray(unwrap(a))?unwrap(a):[]);setLoans(loanList);
   const pairs=await Promise.all(loanList.map(async (x:any)=>{try{const r=await repaymentAPI.schedule(Number(x.id));return [x.id,Array.isArray(unwrap(r))?unwrap(r):[]] as const}catch{return [x.id,[]] as const}})); setSchedules(Object.fromEntries(pairs));
  }catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kupakia taarifa.')}finally{setLoading(false)}
 };
 useEffect(()=>{const token=localStorage.getItem('token'),raw=localStorage.getItem('user');if(!token||!raw){router.push('/login');return}const u=JSON.parse(raw);if(u.role!=='BORROWER'){router.push('/dashboard');return}setUser(u);load()},[router]);

 const submit=async(e:any)=>{e.preventDefault();if(Number(amount)<=0||Number(duration)<=0||!purpose.trim())return;
  setSubmitting(true);setMsg('');
  try{await generalLoanApplicationAPI.submit({amount:Number(amount),duration:Number(duration),purpose:purpose.trim(),termsAccepted:false});
   setAmount('');setDuration('');setPurpose('');await load();setMsg('Ombi limeingia kwenye queue ya lender. Angalia Application Tracker hapa chini.');
  }catch(e:any){setMsg(e?.response?.data?.message||'Ombi halikutumwa.')}finally{setSubmitting(false)}
 };
 const active=loans.filter(x=>['APPROVED','DISBURSED','ACTIVE','DEFAULTED'].includes(x.status));
 const nextDue=active.filter(x=>x.nextDueDate).sort((a,b)=>String(a.nextDueDate).localeCompare(String(b.nextDueDate)))[0];
 const days=nextDue?.nextDueDate?Math.ceil((new Date(nextDue.nextDueDate+'T23:59:59').getTime()-Date.now())/86400000):null;
 const pendingApplication=applications.find(a=>['SUBMITTED','UNDER_REVIEW','OFFER_READY','OFFER_ACCEPTED'].includes(a.status));
 const allSchedule=Object.values(schedules).flat(); const totalPaid=allSchedule.reduce((n:any,x:any)=>n+Number(x.amountPaid||0),0); const totalDue=allSchedule.reduce((n:any,x:any)=>n+Number(x.amountDue||0),0); const balance=Math.max(0,totalDue-totalPaid);
 const stats={applications:applications.length,active:active.length,paid:totalPaid,balance};
 const logout=()=>{localStorage.clear();document.cookie='token=; path=/; max-age=0';router.push('/login')};
 if(!user)return null;

 return <div className="min-h-screen bg-slate-50 text-slate-900 dark:bg-slate-950 dark:text-white">
  <nav className="sticky top-0 z-40 border-b bg-white/95 px-4 py-3 backdrop-blur dark:border-slate-800 dark:bg-slate-900/95">
   <div className="mx-auto flex max-w-7xl items-center justify-between"><Link href="/" className="flex items-center gap-3"><div className="flex h-10 w-10 items-center justify-center rounded-xl bg-blue-600 text-xl text-white">💰</div><div><b>JmkLoanApp</b><div className="text-[10px] text-slate-500">Borrower workspace</div></div></Link><div className="flex items-center gap-2"><LanguageSwitcher/><Link href="/profile" className="hidden rounded-xl px-3 py-2 text-sm font-semibold sm:block">Wasifu</Link><button onClick={logout} className="flex items-center gap-2 rounded-xl px-3 py-2 text-sm font-semibold text-red-600"><LogOut className="h-4 w-4"/>Toka</button></div></div>
  </nav>

  <main className="mx-auto max-w-7xl space-y-6 p-4 sm:p-6">
   <div className="flex flex-wrap gap-2">
    <Link href="/borrower/apply" className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-black text-white">➕ Omba Mkopo</Link>
    <Link href="/loan-guide" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900"><Download className="mr-1 inline h-4 w-4"/>Mkataba & Mwongozo</Link>
    <Link href="/loan-offer" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Offers</Link>
    <Link href="/agreements" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Agreements</Link>
    <Link href="/payments" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Lipa</Link>
    <Link href="/repayments" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Ratiba</Link>
    <button onClick={load} className="rounded-xl border bg-white px-3 py-2 dark:bg-slate-900"><RefreshCw className="h-4 w-4"/></button>
   </div>

   <section className="rounded-3xl bg-gradient-to-br from-blue-700 via-indigo-700 to-slate-950 p-6 text-white shadow-xl">
    <p className="text-sm text-blue-100">Karibu, {user.fullName}</p><h1 className="mt-1 text-3xl font-black">Kituo chako cha mkopo</h1>
    <p className="mt-2 max-w-3xl text-sm text-blue-100">Kila ombi lina Application ID, status na hatua inayofuata. Hapa hutakiwi kubahatisha kama lender ameona ombi lako.</p>
   </section>

   {msg&&<div className="rounded-2xl border border-blue-200 bg-blue-50 p-4 text-sm font-semibold text-blue-800 dark:border-blue-900 dark:bg-blue-950/30 dark:text-blue-200">{msg}</div>}

   <section className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
    {[['Maombi',stats.applications,FileText],['Loans active',stats.active,WalletCards],['Imelipwa',stats.paid,CheckCircle2],['Salio la marejesho',stats.balance,WalletCards]].map(([label,value,I]:any)=><div key={label} className="rounded-2xl border bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-slate-900"><I className="h-5 w-5 text-blue-600"/><div className="mt-3 text-3xl font-black">{value}</div><div className="text-sm text-slate-500">{label}</div></div>)}
   </section>

   <section className="rounded-3xl border border-blue-200 bg-white p-6 shadow-sm dark:border-blue-900 dark:bg-slate-900">
    <div className="flex flex-wrap items-start justify-between gap-3"><div><h2 className="text-xl font-black">Kabla hujaomba</h2><p className="mt-1 text-sm text-slate-500">Soma mkataba/mwongozo kwanza; mkataba wa mwisho utatumia terms halisi za offer yako.</p></div><Link href="/loan-guide" className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-black text-white"><Download className="mr-1 inline h-4 w-4"/>Soma / Pakua</Link></div>
    <div className="mt-4 grid gap-3 md:grid-cols-4">{[['1','Omba kiasi'],['2','Lender review'],['3','Soma offer'],['4','Kubali / saini']].map(([n,x])=><div key={n} className="rounded-2xl border p-4 dark:border-slate-800"><span className="text-xs font-black text-blue-600">HATUA {n}</span><div className="mt-1 font-bold">{x}</div></div>)}</div>
   </section>

   {!pendingApplication&&<section className="rounded-3xl border border-emerald-200 bg-white p-6 shadow-sm dark:border-emerald-900 dark:bg-slate-900">
    <div className="flex items-start gap-3"><WalletCards className="mt-1 text-emerald-600"/><div><h2 className="text-xl font-black">Omba kiasi unachohitaji</h2><p className="mt-1 text-sm text-slate-500">Weka kiasi na muda unaouhitaji. Product, riba, ada na ratiba vitawekwa na lender baada ya review.</p></div></div>
    <form onSubmit={submit} className="mt-5 grid gap-3 md:grid-cols-[1fr_1fr_1.5fr_auto]">
     <input required min="1" type="number" value={amount} onChange={e=>setAmount(e.target.value)} placeholder="Kiasi (TZS)" className="rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/>
     <input required min="1" type="number" value={duration} onChange={e=>setDuration(e.target.value)} placeholder="Muda (siku)" className="rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/>
     <input required maxLength={500} value={purpose} onChange={e=>setPurpose(e.target.value)} placeholder="Unahitaji fedha kwa nini?" className="rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/>
     <button disabled={submitting} className="rounded-xl bg-blue-600 px-5 py-3 font-black text-white disabled:opacity-50">{submitting?'Inatuma...':'Tuma ombi'}</button>
    </form>
   </section>}

   <section className="rounded-3xl border bg-white shadow-sm dark:border-slate-800 dark:bg-slate-900">
    <div className="flex flex-wrap items-center justify-between gap-3 border-b p-5 dark:border-slate-800"><div><h2 className="font-black">Application Tracker</h2><p className="text-sm text-slate-500">Hapa ndipo utaona kama lender ameanza review, ameweka offer au loan imetengenezwa.</p></div>{pendingApplication&&<Link href="/loan-offer" className="rounded-xl bg-blue-600 px-4 py-2 text-xs font-black text-white">Fungua hatua inayofuata</Link>}</div>
    {loading?<div className="p-8 text-center text-sm text-slate-500">Inapakia...</div>:applications.length===0?<div className="p-8 text-sm text-slate-500">Hujaomba mkopo bado.</div>:<div className="divide-y dark:divide-slate-800">{applications.map(a=>{const steps=['SUBMITTED','UNDER_REVIEW','OFFER_READY','OFFER_ACCEPTED','CONVERTED'];const current=Math.max(0,steps.indexOf(a.status));return <article key={a.id} className="p-5"><div className="flex flex-wrap items-start justify-between gap-3"><div><b>Application #{a.id}</b><p className="mt-1 text-sm text-slate-500">{money(a.amount)} · {a.duration} {a.durationUnit||'DAYS'} · {a.purpose}</p></div><span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-black dark:bg-slate-800">{statusLabel[a.status]||a.status}</span></div><div className="mt-4 grid gap-2 sm:grid-cols-3">{steps.map((s,i)=><div key={s} className={'rounded-xl border p-3 text-xs font-bold '+(i<=current?'border-emerald-300 bg-emerald-50 text-emerald-700':'opacity-50 dark:border-slate-800')}><div>{i+1}. {s==='SUBMITTED'?'Imetumwa':s==='UNDER_REVIEW'?'Lender review':s==='OFFER_READY'?'Offer tayari':s==='OFFER_ACCEPTED'?'Offer imekubaliwa':'Loan imetengenezwa'}</div>{i===current&&<div className="mt-1 text-[10px]">← hatua ya sasa</div>}</div>)}</div><div className="mt-4 flex flex-wrap gap-2">{['OFFER_READY','OFFER_ACCEPTED'].includes(a.status)&&<Link href="/loan-offer" className="rounded-xl bg-blue-600 px-3 py-2 text-xs font-black text-white">Angalia Offer</Link>}{a.status==='CONVERTED'&&<Link href="/agreements" className="rounded-xl bg-emerald-600 px-3 py-2 text-xs font-black text-white">Fungua Agreement</Link>}</div></article>})}</div>}
   </section>

   <section className="grid gap-4 lg:grid-cols-3">
    <div className="rounded-3xl border border-amber-200 bg-amber-50 p-5 dark:border-amber-900 dark:bg-amber-950/30"><div className="text-xs font-black uppercase text-amber-700">Deni linalofuata</div><div className="mt-2 text-2xl font-black">{nextDue?money((schedules[nextDue.id]||[]).filter((x:any)=>x.status!=='PAID').reduce((n:any,x:any)=>n+Math.max(0,Number(x.amountDue||0)-Number(x.amountPaid||0)),0)):'—'}</div><div className="mt-1 text-sm">{nextDue?'Loan #'+nextDue.id+' · '+nextDue.nextDueDate:'Hakuna due date'}</div></div>
    <div className="rounded-3xl border border-blue-200 bg-blue-50 p-5 dark:border-blue-900 dark:bg-blue-950/30"><div className="text-xs font-black uppercase text-blue-700">Siku zilizobaki</div><div className="mt-2 text-3xl font-black">{days===null?'—':Math.abs(days)}</div><div className="mt-1 text-sm">{days===null?'Hakuna loan active':days<0?'Siku zimepita tangu due date':'Hadi due date'}</div></div>
    <div className="rounded-3xl border border-emerald-200 bg-emerald-50 p-5 dark:border-emerald-900 dark:bg-emerald-950/30"><div className="text-xs font-black uppercase text-emerald-700">Loan status</div><div className="mt-2 text-xl font-black">{nextDue?.status?statusLabel[nextDue.status]||nextDue.status:'Hakuna loan active'}</div><div className="mt-3 flex flex-wrap gap-2">{nextDue&&<><Link href={'/payments?loanId='+nextDue.id} className="rounded-xl bg-blue-600 px-3 py-2 text-xs font-black text-white"><CreditCard className="mr-1 inline h-3.5 w-3.5"/>Lipa</Link><Link href={'/agreements?loanId='+nextDue.id} className="rounded-xl border px-3 py-2 text-xs font-black"><FileText className="mr-1 inline h-3.5 w-3.5"/>Agreement</Link></>}</div></div>
   </section>

   <section className="rounded-3xl border bg-white shadow-sm dark:border-slate-800 dark:bg-slate-900">
    <div className="border-b p-5 dark:border-slate-800"><h2 className="font-black">Mikopo yangu</h2></div>
    {loading?<div className="p-8 text-center text-sm text-slate-500">Inapakia...</div>:loans.length===0?<div className="p-8 text-sm text-slate-500">Hakuna loan bado.</div>:<div className="divide-y dark:divide-slate-800">{loans.map(l=><div key={l.id} className="flex flex-col gap-3 p-5 lg:flex-row lg:items-center lg:justify-between"><div><div className="flex flex-wrap items-center gap-2"><b>Loan #{l.id}</b><span className="rounded-full bg-slate-100 px-2 py-1 text-xs font-bold dark:bg-slate-800">{statusLabel[l.status]||l.status}</span></div><p className="mt-1 text-sm text-slate-500">Kiasi: <b>{money(l.amount)}</b> · Jumla: <b>{money(l.totalRepayment)}</b> · Due: <b>{l.nextDueDate||'—'}</b></p></div><div className="flex flex-wrap gap-2"><Link href={'/payments?loanId='+l.id} className="rounded-xl bg-blue-600 px-3 py-2 text-xs font-bold text-white">Lipa</Link><Link href={'/agreements?loanId='+l.id} className="rounded-xl border px-3 py-2 text-xs font-bold"><FileSignature className="mr-1 inline h-3.5 w-3.5"/>Agreement</Link><Link href={'/repayments?loanId='+l.id} className="rounded-xl border px-3 py-2 text-xs font-bold">Ratiba</Link></div></div>)}</div>}
   </section>
  </main>
 </div>;
}
