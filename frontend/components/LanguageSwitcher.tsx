'use client';

import { useEffect, useState } from 'react';
import { getInitialLanguage, Lang } from '@/lib/i18n';

export default function LanguageSwitcher() {
  const [lang, setLang] = useState<Lang>('sw');
  useEffect(() => {
    const initial = getInitialLanguage();
    setLang(initial);
    document.documentElement.lang = initial;
  }, []);
  const change = (next: Lang) => {
    setLang(next);
    localStorage.setItem('jmk-language', next);
    document.documentElement.lang = next;
    window.dispatchEvent(new CustomEvent('jmk-language-change', { detail: next }));
  };
  return <div className="inline-flex rounded-xl border border-slate-200 bg-white/80 p-1 text-xs font-bold shadow-sm dark:border-slate-700 dark:bg-slate-900">
    <button type="button" onClick={() => change('sw')} className={`rounded-lg px-2.5 py-1.5 ${lang==='sw'?'bg-blue-600 text-white':'text-slate-600 dark:text-slate-300'}`}>SW</button>
    <button type="button" onClick={() => change('en')} className={`rounded-lg px-2.5 py-1.5 ${lang==='en'?'bg-blue-600 text-white':'text-slate-600 dark:text-slate-300'}`}>EN</button>
  </div>;
}
