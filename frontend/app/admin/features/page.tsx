'use client';

import { useLanguage } from '@/lib/useLanguage';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import Link from 'next/link';

const features=[
 ['📧','Password setup/reset','Usalama wa akaunti','/forgot-password'],
 ['📱','SMS notifications','Arifa za SMS kupitia Twilio','/admin/settings'],
 ['⏰','Overdue reminders','Kikumbusho cha malipo yaliyochelewa','/admin/settings'],
 ['💳','Mobile money','M-Pesa, Tigo Pesa, Airtel Money, HaloPesa','/payments'],
 ['📄','PDF agreements','Mikataba ya mikopo kwa PDF','/agreements'],
 ['✍️','E-signature','Kusaini mikataba kidijitali','/signatures'],
 ['🧾','Audit trail','Rekodi za matukio na mabadiliko','/admin/operations'],
 ['📊','Reports & export','Ripoti na CSV export','/admin/reports'],
 ['🔐','KYC / NIDA','Uthibitishaji kupitia provider rasmi','/admin/users'],
 ['🌍','Swahili ↔ English','Lugha ya mfumo mzima','/admin/settings'],
 ['🛡️','Security controls','Rate limit, validation, secure webhooks, idempotency','/admin/settings'],
 ['👥','Users & roles','Usimamizi salama wa users, roles na accounts','/admin/users'],
];

export default function FeaturesPage(){
 const {lang}=useLanguage(); const en=lang==='en';
 return <div className="p-4 sm:p-6 lg:p-8">
  <div className="mb-8 flex flex-wrap items-center justify-between gap-4">
   <div><p className="text-sm font-bold text-blue-600">JmkLoanApp</p><h1 className="text-3xl font-black text-slate-900 dark:text-white">{en?'Production features':'Vipengele vya Production'}</h1><p className="mt-1 max-w-3xl text-slate-500">{en?'All requested loan-platform capabilities are grouped here. Provider-dependent services require official credentials before live transactions.':'Vipengele vyote ulivyoomba vimepangwa hapa. Huduma zinazohitaji provider rasmi zinahitaji credentials kabla ya miamala ya live.'}</p></div>
   <LanguageSwitcher/>
  </div>
  <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
   {features.map(([icon,title,desc,href])=><Link key={title} href={href} className="group rounded-3xl border border-slate-200 bg-white p-5 shadow-sm transition hover:-translate-y-0.5 hover:shadow-lg dark:border-slate-800 dark:bg-slate-900">
    <div className="flex items-start gap-4"><div className="text-3xl">{icon}</div><div className="min-w-0"><h2 className="font-black text-slate-900 dark:text-white">{en?title:desc}</h2><p className="mt-1 text-sm text-slate-500">{en?desc:title}</p><span className="mt-4 inline-block text-xs font-bold text-blue-600">{en?'Open feature →':'Fungua kipengele →'}</span></div></div>
   </Link>)}
  </div>
 </div>
}
