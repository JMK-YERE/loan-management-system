'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, ArrowRight, CheckCircle2 } from 'lucide-react';
import { authAPI } from '@/lib/api';
import CameraCapture from '@/components/CameraCapture';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { useLanguage } from '@/lib/useLanguage';

const CODES: [string, string][] = [
  ['+255','Tanzania'],['+254','Kenya'],['+256','Uganda'],['+250','Rwanda'],['+257','Burundi'],
  ['+243','DR Congo'],['+260','Zambia'],['+265','Malawi'],['+258','Msumbiji'],['+27','Afrika Kusini'],
  ['+234','Nigeria'],['+233','Ghana'],['+251','Ethiopia'],['+20','Misri'],['+971','UAE'],
  ['+966','Saudi Arabia'],['+91','India'],['+86','China'],['+44','Uingereza'],['+1','USA/Canada'],['other','Nyingine...']
];

type Phone={code:string;custom:string;num:string};
const buildPhone=(p:Phone)=>{
  const raw=p.code==='other'?p.custom.replace(/[^\d+]/g,''):p.code;
  const code=raw.startsWith('+')?raw:'+'+raw;
  return code+p.num.replace(/\D/g,'').replace(/^0+/,'');
};
const cls='w-full rounded-xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none focus:ring-2 focus:ring-blue-500 dark:border-slate-700 dark:bg-slate-950 dark:text-white';
const Field=({label,children}:{label:string;children:React.ReactNode})=><label className="block text-sm font-bold text-slate-700 dark:text-slate-200">{label}<div className="mt-1.5">{children}</div></label>;
const PhoneField=({value,onChange}:{value:Phone;onChange:(v:Phone)=>void})=><div className="flex gap-2"><select value={value.code} onChange={e=>onChange({...value,code:e.target.value})} className="w-36 shrink-0 rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950">{CODES.map(([c,n])=><option key={c} value={c}>{c==='other'?n:`${n} ${c}`}</option>)}</select><input required type="tel" value={value.num} onChange={e=>onChange({...value,num:e.target.value})} className={cls} placeholder="7XXXXXXXX"/></div>;

export default function RegisterPage(){
 const router=useRouter();const {lang}=useLanguage();const en=lang==='en';
 const [step,setStep]=useState(1);
 const [f,setF]=useState({fullName:'',email:'',role:'BORROWER',dateOfBirth:'',gender:'',maritalStatus:'',nationality:'',idType:'NIDA',idNumber:'',address:'',city:'',country:'Tanzania',employmentStatus:'',occupation:'',employer:'',monthlyIncome:'',kinName:'',kinRelationship:''});
 const [phone,setPhone]=useState<Phone>({code:'+255',custom:'',num:''});const [kinPhone,setKinPhone]=useState<Phone>({code:'+255',custom:'',num:''});
 const [photo,setPhoto]=useState('');const [consent,setConsent]=useState(false);const [loading,setLoading]=useState(false);const [error,setError]=useState('');const [success,setSuccess]=useState('');
 const set=(k:keyof typeof f)=>(e:React.ChangeEvent<HTMLInputElement|HTMLSelectElement>)=>setF({...f,[k]:e.target.value});
 const validateStep=()=>{
   setError('');
   if(step===1 && !photo) return setError('Picha ya pasipoti inahitajika.');
   if(step===1 && (!f.fullName||!f.dateOfBirth||!f.gender||!f.maritalStatus||!f.nationality)) return setError('Jaza taarifa zote za msingi.');
   if(step===2 && (!f.email||!phone.num||!f.idNumber||!f.address||!f.city)) return setError('Jaza mawasiliano na kitambulisho.');
   if(step===3 && (!f.employmentStatus||!f.monthlyIncome)) return setError('Jaza taarifa za ajira na kipato.');
   if(step===4 && (!f.kinName||!f.kinRelationship||!kinPhone.num)) return setError('Jaza taarifa za ndugu wa karibu.');
   return true;
 };
 const next=()=>{if(validateStep())setStep(s=>Math.min(5,s+1))};
 const back=()=>{setError('');setStep(s=>Math.max(1,s-1))};
 const submit=async(e:React.FormEvent)=>{e.preventDefault();if(!validateStep())return;if(!consent)return setError('Kubali uthibitisho wa taarifa zako.');setLoading(true);setError('');try{
   const payload={...f,phone:buildPhone(phone),kinPhone:buildPhone(kinPhone),monthlyIncome:Number(f.monthlyIncome),photo};
   const res=await authAPI.register(payload);setSuccess(res.data.message||'Usajili umepokelewa.');window.scrollTo({top:0,behavior:'smooth'});setTimeout(()=>router.push('/login'),4500);
 }catch(err:any){const d=err.response?.data;let m=d?.message||'Usajili umeshindikana.';if(d?.data&&typeof d.data==='object')m+='\n'+Object.values(d.data).join('\n');setError(m);}finally{setLoading(false)}};
 const titles=en?['Identity','Contact & ID','Work & income','Emergency contact','Review']:['Utambulisho','Mawasiliano & ID','Ajira & kipato','Ndugu wa karibu','Kagua taarifa'];
 return <main className="min-h-screen bg-slate-50 p-4 dark:bg-slate-950 sm:p-8">
  <div className="mx-auto max-w-3xl">
   <div className="mb-5 flex items-center justify-between"><Link href="/" className="font-black">💰 JmkLoanApp</Link><LanguageSwitcher/></div>
   <section className="mb-5 rounded-3xl bg-gradient-to-br from-blue-700 to-slate-950 p-6 text-white">
    <div className="flex items-start justify-between gap-4"><div><p className="text-sm text-blue-200">Account onboarding</p><h1 className="mt-1 text-3xl font-black">{en?'Create account':'Fungua akaunti'}</h1><p className="mt-2 text-sm text-blue-100">Jaza sehemu moja baada ya nyingine. Unaweza kurudi nyuma bila kupoteza taarifa ulizoandika.</p></div><Link href="/loan-guide" className="rounded-xl border border-white/30 px-3 py-2 text-xs font-bold">📄 Mkataba</Link></div>
    <div className="mt-6 grid grid-cols-5 gap-2">{titles.map((x,i)=><button type="button" key={x} onClick={()=>i+1<step&&setStep(i+1)} className={`rounded-xl p-2 text-left ${i+1===step?'bg-white text-blue-700':'bg-white/10 text-white'}`}><div className="text-[10px] font-black">STEP {i+1}</div><div className="mt-1 truncate text-xs font-bold">{x}</div></button>)}</div>
   </section>
   {error&&<div className="mb-4 whitespace-pre-line rounded-2xl border border-red-200 bg-red-50 p-4 text-sm font-semibold text-red-700">⚠️ {error}</div>}
   {success&&<div className="mb-4 rounded-2xl border border-emerald-200 bg-emerald-50 p-4 text-sm font-semibold text-emerald-700">✅ {success}</div>}
   <form onSubmit={submit} className="rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
    {step===1&&<div className="space-y-5"><h2 className="text-xl font-black">{titles[0]}</h2><CameraCapture value={photo} onChange={setPhoto}/><Field label="Jina kamili"><input required value={f.fullName} onChange={set('fullName')} className={cls}/></Field><div className="grid gap-4 sm:grid-cols-2"><Field label="Tarehe ya kuzaliwa"><input required type="date" value={f.dateOfBirth} onChange={set('dateOfBirth')} className={cls}/></Field><Field label="Jinsia"><select required value={f.gender} onChange={set('gender')} className={cls}><option value="">Chagua</option><option value="MALE">Me</option><option value="FEMALE">Ke</option></select></Field><Field label="Hali ya ndoa"><select required value={f.maritalStatus} onChange={set('maritalStatus')} className={cls}><option value="">Chagua</option><option value="SINGLE">Sijaoa/Sijaolewa</option><option value="MARRIED">Nimeoa/Nimeolewa</option><option value="DIVORCED">Nimeachana</option><option value="WIDOWED">Mjane/Mgane</option></select></Field><Field label="Utaifa"><input required value={f.nationality} onChange={set('nationality')} className={cls}/></Field></div></div>}
    {step===2&&<div className="space-y-5"><h2 className="text-xl font-black">{titles[1]}</h2><div className="grid gap-4 sm:grid-cols-2"><Field label="Barua pepe"><input required type="email" value={f.email} onChange={set('email')} className={cls}/></Field><Field label="Simu"><PhoneField value={phone} onChange={setPhone}/></Field><Field label="Aina ya kitambulisho"><select value={f.idType} onChange={set('idType')} className={cls}><option>NIDA</option><option>PASSPORT</option><option>DRIVING_LICENSE</option><option>VOTER_ID</option><option>OTHER</option></select></Field><Field label="Namba ya kitambulisho"><input required minLength={5} maxLength={30} value={f.idNumber} onChange={set('idNumber')} className={cls}/></Field><Field label="Mtaa / Kata"><input required value={f.address} onChange={set('address')} className={cls}/></Field><Field label="Mji"><input required value={f.city} onChange={set('city')} className={cls}/></Field><Field label="Nchi"><input required value={f.country} onChange={set('country')} className={cls}/></Field></div></div>}
    {step===3&&<div className="space-y-5"><h2 className="text-xl font-black">{titles[2]}</h2><div className="grid gap-4 sm:grid-cols-2"><Field label="Hali ya ajira"><select required value={f.employmentStatus} onChange={set('employmentStatus')} className={cls}><option value="">Chagua</option><option value="EMPLOYED">Nimeajiriwa</option><option value="SELF_EMPLOYED">Najiajiri / Biashara</option><option value="STUDENT">Mwanafunzi</option><option value="RETIRED">Mstaafu</option><option value="UNEMPLOYED">Sina ajira</option></select></Field><Field label="Kazi / shughuli"><input value={f.occupation} onChange={set('occupation')} className={cls}/></Field><Field label="Mwajiri / biashara"><input value={f.employer} onChange={set('employer')} className={cls}/></Field><Field label="Kipato cha mwezi (TZS)"><input required min="0" type="number" value={f.monthlyIncome} onChange={set('monthlyIncome')} className={cls}/></Field></div></div>}
    {step===4&&<div className="space-y-5"><h2 className="text-xl font-black">{titles[3]}</h2><div className="rounded-2xl bg-blue-50 p-4 text-sm text-blue-800 dark:bg-blue-950/30 dark:text-blue-200">Huyu ni mtu wa karibu wa mawasiliano. Mdhamini wa loan atachaguliwa/kuombwa kwenye workflow ya mkopo, siyo hapa kwa lazima.</div><div className="grid gap-4 sm:grid-cols-2"><Field label="Jina la ndugu wa karibu"><input required value={f.kinName} onChange={set('kinName')} className={cls}/></Field><Field label="Uhusiano"><input required value={f.kinRelationship} onChange={set('kinRelationship')} className={cls}/></Field><Field label="Simu ya ndugu wa karibu"><PhoneField value={kinPhone} onChange={setKinPhone}/></Field><Field label="Aina ya akaunti"><select value={f.role} onChange={set('role')} className={cls}><option value="BORROWER">Mkopaji</option><option value="LENDER">Mkopeshaji</option><option value="GUARANTOR">Mdhamini</option></select></Field></div></div>}
    {step===5&&<div className="space-y-5"><h2 className="text-xl font-black">{titles[4]}</h2><div className="grid gap-3 sm:grid-cols-2">{[['Jina',f.fullName],['Email',f.email],['Simu',buildPhone(phone)],['ID',f.idNumber],['Mji',f.city],['Role',f.role],['Kipato',`TZS ${Number(f.monthlyIncome||0).toLocaleString()}`]].map(([k,v])=><div key={k} className="rounded-xl border p-4 dark:border-slate-800"><div className="text-xs text-slate-500">{k}</div><div className="mt-1 font-bold">{v||'-'}</div></div>)}</div><label className="flex items-start gap-3 rounded-2xl border p-4 text-sm dark:border-slate-800"><input type="checkbox" checked={consent} onChange={e=>setConsent(e.target.checked)} className="mt-1 h-5 w-5"/><span>Nathibitisha kuwa taarifa na picha nilizotoa ni sahihi na nakubali zitumike kwa onboarding/KYC na mawasiliano ya akaunti.</span></label><div className="rounded-2xl bg-slate-50 p-4 text-sm text-slate-600 dark:bg-slate-950 dark:text-slate-300">Baada ya kutuma, admin atakagua akaunti. Ikiidhinishwa, utapokea email halisi yenye link ya kuweka password.</div></div>}
    <div className="mt-7 flex flex-wrap justify-between gap-3"><button type="button" onClick={step===1?()=>router.push('/'):back} className="inline-flex items-center gap-2 rounded-xl border px-4 py-3 font-bold"><ArrowLeft className="h-4 w-4"/>{step===1?'Rudi Landing':'Rudi'}</button>{step<5?<button type="button" onClick={next} className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-5 py-3 font-black text-white">Endelea <ArrowRight className="h-4 w-4"/></button>:<button disabled={loading} className="inline-flex items-center gap-2 rounded-xl bg-emerald-600 px-5 py-3 font-black text-white disabled:opacity-50">{loading?'Inatuma...':'Tuma usajili'} <CheckCircle2 className="h-4 w-4"/></button>}</div>
   </form>
   <p className="mt-5 text-center text-sm text-slate-500">Una akaunti? <Link href="/login" className="font-bold text-blue-600">Ingia</Link></p>
  </div>
 </main>;
}
