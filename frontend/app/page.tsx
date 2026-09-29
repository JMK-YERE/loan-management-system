'use client';

import Image from 'next/image';
import Link from 'next/link';
import { useEffect, useState } from 'react';
import {
  ArrowRight, Calculator, CheckCircle2, CreditCard, FileCheck2,
  LockKeyhole, Menu, ShieldCheck, Users, X,
} from 'lucide-react';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { getInitialLanguage, translations, Lang } from '@/lib/i18n';

const images = [
  'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=1400&q=85',
  'https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=1400&q=85',
  'https://images.unsplash.com/photo-1563986768609-322da13575f3?w=1400&q=85',
  'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=1400&q=85',
  'https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=1400&q=85',
];

const iconMap: Record<string, any> = { Calculator, Workflow: FileCheck2, Payments: CreditCard, Guarantor: Users, Security: ShieldCheck, Reports: CheckCircle2 };

export default function Home() {
  const [lang, setLang] = useState<Lang>('sw');
  const [slide, setSlide] = useState(0);
  const [menu, setMenu] = useState(false);
  const t = translations[lang];

  useEffect(() => {
    const initial = getInitialLanguage();
    setLang(initial);
    const handler = (event: Event) => setLang((event as CustomEvent<Lang>).detail);
    window.addEventListener('jmk-language-change', handler);
    return () => window.removeEventListener('jmk-language-change', handler);
  }, []);

  useEffect(() => {
    const timer = window.setInterval(() => setSlide((s) => (s + 1) % images.length), 5500);
    return () => window.clearInterval(timer);
  }, []);

  return (
    <main className="min-h-screen overflow-x-hidden bg-white text-slate-900 dark:bg-slate-950 dark:text-white">
      <nav className="sticky top-0 z-50 border-b border-slate-200/80 bg-white/90 backdrop-blur-xl dark:border-slate-800 dark:bg-slate-950/90">
        <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6">
          <Link href="/" className="flex items-center gap-2.5">
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-blue-600 to-indigo-700 text-xl text-white shadow-lg">💰</div>
            <span className="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-lg font-black text-transparent">JmkLoanApp</span>
          </Link>
          <div className="hidden items-center gap-7 md:flex">
            <a href="#features" className="text-sm font-semibold text-slate-600 hover:text-blue-600 dark:text-slate-300">{t.nav.features}</a>
            <a href="#how" className="text-sm font-semibold text-slate-600 hover:text-blue-600 dark:text-slate-300">{t.nav.how}</a>
            <a href="#security" className="text-sm font-semibold text-slate-600 hover:text-blue-600 dark:text-slate-300">{t.nav.security}</a>
            <a href="#contact" className="text-sm font-semibold text-slate-600 hover:text-blue-600 dark:text-slate-300">{t.nav.contact}</a>
          </div>
          <div className="flex items-center gap-2">
            <LanguageSwitcher />
            <Link href="/login" className="hidden rounded-xl px-3 py-2 text-sm font-bold text-slate-700 hover:bg-slate-100 sm:block dark:text-slate-200 dark:hover:bg-slate-800">{t.nav.login}</Link>
            <Link href="/register" className="hidden rounded-xl bg-blue-600 px-4 py-2 text-sm font-bold text-white shadow-lg hover:bg-blue-700 sm:block">{t.nav.register}</Link>
            <button onClick={() => setMenu(!menu)} className="rounded-xl p-2 md:hidden" aria-label="Menu">{menu ? <X /> : <Menu />}</button>
          </div>
        </div>
        {menu && <div className="border-t border-slate-200 bg-white p-4 md:hidden dark:border-slate-800 dark:bg-slate-950">
          <div className="grid gap-2">
            <a href="#features" onClick={() => setMenu(false)} className="rounded-xl p-3 font-semibold">{t.nav.features}</a>
            <a href="#how" onClick={() => setMenu(false)} className="rounded-xl p-3 font-semibold">{t.nav.how}</a>
            <a href="#security" onClick={() => setMenu(false)} className="rounded-xl p-3 font-semibold">{t.nav.security}</a>
            <Link href="/login" className="rounded-xl p-3 font-semibold">{t.nav.login}</Link>
            <Link href="/register" className="rounded-xl bg-blue-600 p-3 text-center font-bold text-white">{t.nav.register}</Link>
          </div>
        </div>}
      </nav>

      <section className="relative min-h-[680px] overflow-hidden bg-slate-950">
        <Image src={images[slide]} alt="Loan management" fill priority className="object-cover opacity-35 transition-all duration-700" sizes="100vw" />
        <div className="absolute inset-0 bg-gradient-to-r from-slate-950 via-slate-950/80 to-blue-950/50" />
        <div className="relative mx-auto grid min-h-[680px] max-w-7xl items-center gap-12 px-4 py-16 sm:px-6 lg:grid-cols-[1.05fr_.95fr]">
          <div className="max-w-3xl text-white">
            <div className="mb-5 inline-flex items-center gap-2 rounded-full border border-white/20 bg-white/10 px-4 py-2 text-xs font-bold tracking-wider backdrop-blur">
              <ShieldCheck className="h-4 w-4 text-cyan-300" /> {t.hero.badge}
            </div>
            <h1 className="text-4xl font-black leading-tight sm:text-5xl lg:text-6xl">{t.hero.title}</h1>
            <p className="mt-6 max-w-2xl text-base leading-8 text-slate-200 sm:text-xl">{t.hero.text}</p>
            <div className="mt-8 flex flex-wrap gap-3">
              <Link href="/register" className="inline-flex items-center gap-2 rounded-2xl bg-blue-600 px-6 py-3.5 font-black text-white shadow-xl hover:bg-blue-500">{t.hero.primary}<ArrowRight className="h-5 w-5" /></Link>
              <Link href="/login" className="rounded-2xl border border-white/25 bg-white/10 px-6 py-3.5 font-bold text-white backdrop-blur hover:bg-white/20">{t.hero.secondary}</Link>
            </div>
            <div className="mt-8 flex flex-wrap gap-x-6 gap-y-3 text-sm text-slate-300">
              {t.trust.map((x) => <span key={x} className="flex items-center gap-2"><CheckCircle2 className="h-4 w-4 text-emerald-400" />{x}</span>)}
            </div>
          </div>
          <div className="relative hidden lg:block">
            <div className="relative mx-auto h-[430px] w-[370px] overflow-hidden rounded-[2rem] border border-white/20 shadow-2xl">
              <Image src={images[(slide + 1) % images.length]} alt="Digital finance" fill className="object-cover" sizes="370px" />
              <div className="absolute inset-x-5 bottom-5 rounded-2xl border border-white/20 bg-slate-950/70 p-5 text-white backdrop-blur-xl">
                <div className="text-xs font-bold uppercase tracking-widest text-cyan-300">{lang === 'sw' ? 'Mfumo mmoja' : 'One platform'}</div>
                <div className="mt-1 text-2xl font-black">{lang === 'sw' ? 'Mikopo • Malipo • Wadhamini' : 'Loans • Payments • Guarantors'}</div>
              </div>
            </div>
          </div>
        </div>
        <div className="absolute bottom-7 left-1/2 flex -translate-x-1/2 gap-2">
          {images.map((_, i) => <button key={i} onClick={() => setSlide(i)} aria-label={`Slide ${i + 1}`} className={`h-2 rounded-full transition-all ${i === slide ? 'w-9 bg-white' : 'w-2 bg-white/40'}`} />)}
        </div>
      </section>

      <section id="features" className="bg-slate-50 py-20 dark:bg-slate-900/50">
        <div className="mx-auto max-w-7xl px-4 sm:px-6">
          <div className="mx-auto max-w-2xl text-center">
            <p className="font-bold text-blue-600">JmkLoanApp</p>
            <h2 className="mt-2 text-3xl font-black sm:text-4xl">{t.featuresTitle}</h2>
            <p className="mt-3 text-slate-600 dark:text-slate-400">{t.featuresSub}</p>
          </div>
          <div className="mt-12 grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {t.features.map(([key, title, desc], i) => {
              const Icon = iconMap[key] || CheckCircle2;
              return <article key={key} className="group overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-sm transition hover:-translate-y-1 hover:shadow-xl dark:border-slate-800 dark:bg-slate-950">
                <div className="relative h-40 overflow-hidden">
                  <Image src={images[i % images.length]} alt={title} fill className="object-cover transition duration-500 group-hover:scale-105" sizes="(max-width: 1024px) 50vw, 33vw" />
                  <div className="absolute inset-0 bg-gradient-to-t from-slate-950/80 to-transparent" />
                  <div className="absolute bottom-4 left-4 flex h-11 w-11 items-center justify-center rounded-xl bg-white text-blue-600 shadow-xl"><Icon className="h-5 w-5" /></div>
                </div>
                <div className="p-6"><h3 className="text-lg font-black">{title}</h3><p className="mt-2 text-sm leading-6 text-slate-600 dark:text-slate-400">{desc}</p></div>
              </article>;
            })}
          </div>
        </div>
      </section>

      <section id="how" className="py-20">
        <div className="mx-auto max-w-7xl px-4 sm:px-6">
          <div className="text-center"><h2 className="text-3xl font-black sm:text-4xl">{t.howTitle}</h2><p className="mt-3 text-slate-600 dark:text-slate-400">{t.howSub}</p></div>
          <div className="mt-12 grid gap-5 md:grid-cols-4">
            {t.steps.map(([num,title,desc]) => <div key={num} className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
              <div className="text-4xl font-black text-blue-600">{num}</div><h3 className="mt-5 font-black">{title}</h3><p className="mt-2 text-sm leading-6 text-slate-600 dark:text-slate-400">{desc}</p>
            </div>)}
          </div>
        </div>
      </section>

      <section id="security" className="bg-slate-950 py-20 text-white">
        <div className="mx-auto grid max-w-7xl items-center gap-12 px-4 sm:px-6 lg:grid-cols-2">
          <div><div className="mb-5 flex h-14 w-14 items-center justify-center rounded-2xl bg-blue-600"><LockKeyhole /></div><h2 className="text-3xl font-black sm:text-4xl">{t.securityTitle}</h2><p className="mt-4 leading-7 text-slate-300">{t.securitySub}</p>
            <div className="mt-7 grid gap-3 sm:grid-cols-2">{['JWT authentication','Role-based authorization','Protected API routes','Server-side validation'].map(x => <div key={x} className="rounded-2xl border border-white/10 bg-white/5 p-4 font-semibold"><CheckCircle2 className="mr-2 inline h-4 w-4 text-emerald-400" />{x}</div>)}</div>
          </div>
          <div className="relative h-[360px] overflow-hidden rounded-[2rem] border border-white/10"><Image src={images[4]} alt="Secure digital finance" fill className="object-cover" sizes="(max-width: 1024px) 100vw, 50vw" /><div className="absolute inset-0 bg-blue-950/40" /></div>
        </div>
      </section>

      <section id="contact" className="bg-gradient-to-br from-blue-700 to-indigo-900 py-20 text-white">
        <div className="mx-auto max-w-4xl px-4 text-center sm:px-6"><h2 className="text-3xl font-black sm:text-4xl">{t.ctaTitle}</h2><p className="mx-auto mt-4 max-w-2xl text-blue-100">{t.ctaText}</p><Link href="/register" className="mt-8 inline-flex items-center gap-2 rounded-2xl bg-white px-7 py-3.5 font-black text-blue-700 shadow-xl hover:bg-blue-50">{t.cta}<ArrowRight className="h-5 w-5" /></Link></div>
      </section>

      <footer className="border-t border-slate-200 bg-white py-8 dark:border-slate-800 dark:bg-slate-950">
        <div className="mx-auto flex max-w-7xl flex-col items-center justify-between gap-4 px-4 text-sm text-slate-500 sm:flex-row sm:px-6"><span>{t.footer}</span><div className="flex gap-5"><Link href="/login" className="hover:text-blue-600">{t.nav.login}</Link><Link href="/register" className="hover:text-blue-600">{t.nav.register}</Link></div></div>
      </footer>
    </main>
  );
}
