'use client';

import { useEffect, useState } from 'react';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { getInitialLanguage } from '@/lib/i18n';

export default function SettingsPage() {
  const [lang,setLang]=useState<'sw'|'en'>('sw');
  const [dark,setDark]=useState(false);
  useEffect(()=>{setLang(getInitialLanguage());setDark(document.documentElement.classList.contains('dark'));},[]);
  const setTheme=(value:boolean)=>{setDark(value);document.documentElement.classList.toggle('dark',value);localStorage.setItem('jmk-theme',value?'dark':'light');};
  const en=lang==='en';
  return <div className="p-4 sm:p-6 lg:p-8">
    <div className="mb-8 flex flex-wrap items-center justify-between gap-4">
      <div><p className="text-sm font-bold text-blue-600">JmkLoanApp</p><h1 className="text-3xl font-black text-slate-900 dark:text-white">{en?'System settings':'Mipangilio ya mfumo'}</h1><p className="mt-1 text-slate-500">{en?'Control the administrator workspace preferences.':'Dhibiti mapendeleo ya dashboard ya msimamizi.'}</p></div>
      <LanguageSwitcher/>
    </div>
    <div className="grid gap-5 lg:grid-cols-2">
      <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
        <h2 className="text-xl font-black">{en?'Language':'Lugha'}</h2>
        <p className="mt-1 text-sm text-slate-500">{en?'The app can detect your browser language and you can switch manually.':'Mfumo unatambua lugha ya browser na unaweza kubadilisha mwenyewe.'}</p>
        <div className="mt-5 flex items-center justify-between rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><span className="font-bold">{en?'Current language':'Lugha ya sasa'}</span><b>{lang==='sw'?'Kiswahili':'English'}</b></div>
      </section>
      <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
        <h2 className="text-xl font-black">{en?'Appearance':'Muonekano'}</h2>
        <p className="mt-1 text-sm text-slate-500">{en?'Choose the dashboard appearance on this device.':'Chagua muonekano wa dashboard kwenye kifaa hiki.'}</p>
        <div className="mt-5 flex items-center justify-between rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><div><b>Dark mode</b><p className="text-xs text-slate-500">{en?'Saved locally on this device.':'Huhifadhiwa kwenye kifaa hiki.'}</p></div><button onClick={()=>setTheme(!dark)} className={`relative h-7 w-12 rounded-full ${dark?'bg-blue-600':'bg-slate-300'}`}><span className={`absolute top-1 h-5 w-5 rounded-full bg-white transition ${dark?'left-6':'left-1'}`}/></button></div>
      </section>
      <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900 lg:col-span-2">
        <h2 className="text-xl font-black">{en?'System information':'Taarifa za mfumo'}</h2>
        <div className="mt-5 grid gap-3 sm:grid-cols-3">
          <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><span className="text-xs text-slate-500">Frontend</span><b className="mt-1 block">Next.js</b></div>
          <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><span className="text-xs text-slate-500">Backend</span><b className="mt-1 block">Spring Boot</b></div>
          <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><span className="text-xs text-slate-500">Database</span><b className="mt-1 block">PostgreSQL</b></div>
        </div>
      </section>
    </div>
  </div>;
}
