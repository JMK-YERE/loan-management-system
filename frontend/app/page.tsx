'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import {
  ArrowRight, Shield, Zap, Users, TrendingUp, Star, Phone,
  MapPin, MessageCircle, Facebook, Instagram, Twitter,
  CheckCircle, ChevronLeft, ChevronRight, Megaphone,
} from 'lucide-react';

export default function Home() {
  const [currentSlide, setCurrentSlide] = useState(0);

  const slides = [
    {
      title: 'Karibu JmkLoanApp',
      subtitle: 'Mfumo wa kisasa wa mikopo Tanzania',
      content: 'Kopesha kwa urahisi, fuatilia marejesho, na pata faida. Kila kitu mahali pamoja.',
      tag: 'Karibu',
      color: 'from-blue-600 via-indigo-600 to-purple-700',
      cta: 'Anza Sasa',
      link: '/register',
    },
    {
      title: 'Riba 5% kwa Wiki Hii',
      subtitle: 'Ofa maalum kwa wateja wapya',
      content: 'Pata mkopo kwa riba nafuu ya 5% kwa wiki hii. Ofa inaisha hivi karibuni!',
      tag: 'Ofa',
      color: 'from-orange-500 via-red-500 to-pink-600',
      cta: 'Chukua Ofa',
      link: '/register',
    },
    {
      title: 'Malipo kwa M-Pesa, Tigo, Airtel',
      subtitle: 'Njia zote za malipo zinapatikana',
      content: 'Lipa kwa urahisi kupitia M-Pesa, Tigo Pesa, Airtel Money, au HaloPesa.',
      tag: 'Malipo',
      color: 'from-green-500 via-emerald-600 to-teal-700',
      cta: 'Jifunze Zaidi',
      link: '#features',
    },
    {
      title: 'Uthibitisho wa NIDA',
      subtitle: 'Usalama wa hali ya juu',
      content: 'Kila mtumiaji anathibitishwa kwa NIDA. Data yako iko salama.',
      tag: 'Usalama',
      color: 'from-purple-600 via-pink-600 to-rose-600',
      cta: 'Jisajili',
      link: '/register',
    },
  ];

  const news = [
    {
      title: 'JmkLoanApp yazinduliwa Tanzania',
      content: 'Mfumo mpya wa mikopo unalenga kurahisisha biashara ya kukopesha.',
      date: '27 Sep 2026',
      tag: 'Habari',
    },
    {
      title: 'Wadhamini sasa kwa simu',
      content: 'Wadhamini wanaweza kuidhinisha mikopo kupitia simu zao moja kwa moja.',
      date: '26 Sep 2026',
      tag: 'Sasisho',
    },
    {
      title: 'Ripoti za faida kila wiki',
      content: 'Pata ripoti kamili ya faida na mikopo inayochelewa kila wiki.',
      date: '25 Sep 2026',
      tag: 'Kipengele',
    },
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % slides.length);
    }, 5000);
    return () => clearInterval(interval);
  }, [slides.length]);

  const nextSlide = () => setCurrentSlide((prev) => (prev + 1) % slides.length);
  const prevSlide = () => setCurrentSlide((prev) => (prev - 1 + slides.length) % slides.length);

  const features = [
    { icon: Shield, title: 'Usalama wa Hali ya Juu', desc: 'Data yako inalindwa kwa encryption ya kisasa na uthibitisho wa NIDA.', color: 'from-blue-500 to-indigo-600' },
    { icon: Zap, title: 'Haraka na Rahisi', desc: 'Omba mkopo kwa dakika 2 tu. Uthibitisho unakuja haraka.', color: 'from-yellow-500 to-orange-500' },
    { icon: Users, title: 'Wadhamini', desc: 'Ongeza wadhamini kwa urahisi. Wanaidhinisha kwa simu.', color: 'from-green-500 to-emerald-600' },
    { icon: TrendingUp, title: 'Ripoti za Kina', desc: 'Ona faida, mikopo inayochelewa, na historia yote.', color: 'from-purple-500 to-pink-600' },
  ];

  const steps = [
    { num: '1', title: 'Jisajili', desc: 'Jaza taarifa zako — NIDA, email, simu.' },
    { num: '2', title: 'Subiri Uthibitisho', desc: 'Admin anakagua na kukutumia email.' },
    { num: '3', title: 'Weka Password', desc: 'Bonyeza link kwenye email, weka password.' },
    { num: '4', title: 'Anza Kukopesha', desc: 'Ingia, ongeza wakopaji, toa mikopo.' },
  ];

  const testimonials = [
    { name: 'Joseph M.', role: 'Mkopeshaji', text: 'JmkLoanApp imerahisisha biashara yangu. Sasa nafuatilia mikopo yote kwa simu.' },
    { name: 'Neema K.', role: 'Mkopaji', text: 'Nilipata mkopo haraka bila usumbufu. Malipo kwa M-Pesa ni rahisi sana.' },
    { name: 'Amina S.', role: 'Mdhamini', text: 'Kuidhinisha mikopo kwa wateja wangu ni rahisi. Nashukuru sana.' },
  ];

  const currentSlideData = slides[currentSlide];

  return (
    <main className="min-h-screen bg-white dark:bg-gray-950">
      {/* NAVBAR */}
      <nav className="sticky top-0 z-50 bg-white/90 dark:bg-gray-950/90 backdrop-blur-lg border-b border-gray-200 dark:border-gray-800 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <Link href="/" className="flex items-center gap-2">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-xl shadow-lg">
                💰
              </div>
              <span className="text-xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
                JmkLoanApp
              </span>
            </Link>
            <div className="hidden md:flex items-center gap-8">
              <a href="#features" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 transition font-medium">Vipengele</a>
              <a href="#how" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 transition font-medium">Jinsi Inavyofanya Kazi</a>
              <a href="#news" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 transition font-medium">Habari</a>
              <a href="#contact" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 transition font-medium">Wasiliana</a>
            </div>
            <div className="flex items-center gap-3">
              <Link href="/login" className="px-4 py-2 text-sm font-semibold text-gray-700 dark:text-gray-200 hover:text-blue-600 transition">Ingia</Link>
              <Link href="/register" className="px-5 py-2 text-sm font-semibold bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white rounded-xl transition shadow-lg hover:shadow-xl">Jisajili</Link>
            </div>
          </div>
        </div>
      </nav>

      {/* HERO SLIDER */}
      <section className="relative overflow-hidden">
        <div className={`relative bg-gradient-to-br ${currentSlideData.color} transition-all duration-700 min-h-[500px] flex items-center`}>
          {/* Animated circles */}
          <div className="absolute top-0 right-0 w-96 h-96 bg-white/10 rounded-full blur-3xl animate-pulse" />
          <div className="absolute bottom-0 left-0 w-96 h-96 bg-white/10 rounded-full blur-3xl animate-pulse" style={{ animationDelay: '1s' }} />

          <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 w-full">
            <div className="grid md:grid-cols-2 gap-12 items-center">
              <div className="text-white">
                <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-white/20 backdrop-blur-sm text-white text-sm font-semibold mb-6 shadow-sm">
                  <Megaphone className="w-4 h-4" />
                  {currentSlideData.tag}
                </div>
                <h1 className="text-4xl md:text-5xl font-extrabold mb-4 leading-tight animate-fade-in">
                  {currentSlideData.title}
                </h1>
                <p className="text-xl md:text-2xl font-semibold mb-4 text-white/90">
                  {currentSlideData.subtitle}
                </p>
                <p className="text-lg text-white/80 mb-8 leading-relaxed">
                  {currentSlideData.content}
                </p>
                <Link href={currentSlideData.link} className="inline-flex items-center gap-2 px-8 py-4 bg-white text-gray-900 font-bold rounded-xl hover:bg-gray-100 transition shadow-xl hover:scale-105 transform">
                  {currentSlideData.cta}
                  <ArrowRight className="w-5 h-5" />
                </Link>
              </div>
              <div className="hidden md:flex justify-center">
                <div className="w-80 h-80 rounded-3xl bg-white/20 backdrop-blur-lg border border-white/30 flex items-center justify-center shadow-2xl animate-float">
                  <span className="text-9xl">💰</span>
                </div>
              </div>
            </div>
          </div>

          {/* Navigation Arrows */}
          <button onClick={prevSlide} className="absolute left-4 top-1/2 -translate-y-1/2 w-12 h-12 rounded-full bg-white/20 backdrop-blur-sm hover:bg-white/30 flex items-center justify-center text-white transition" aria-label="Previous">
            <ChevronLeft className="w-6 h-6" />
          </button>
          <button onClick={nextSlide} className="absolute right-4 top-1/2 -translate-y-1/2 w-12 h-12 rounded-full bg-white/20 backdrop-blur-sm hover:bg-white/30 flex items-center justify-center text-white transition" aria-label="Next">
            <ChevronRight className="w-6 h-6" />
          </button>

          {/* Dots */}
          <div className="absolute bottom-6 left-1/2 -translate-x-1/2 flex gap-2">
            {slides.map((_, i) => (
              <button key={i} onClick={() => setCurrentSlide(i)} className={`h-2 rounded-full transition-all duration-300 ${i === currentSlide ? 'w-8 bg-white' : 'w-2 bg-white/50 hover:bg-white/80'}`} aria-label={`Slide ${i + 1}`} />
            ))}
          </div>
        </div>
      </section>

      {/* TRUST BADGES */}
      <section className="py-8 bg-gray-50 dark:bg-gray-900 border-b border-gray-200 dark:border-gray-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-wrap justify-center gap-8 text-sm text-gray-600 dark:text-gray-400">
            <div className="flex items-center gap-2"><CheckCircle className="w-5 h-5 text-green-500" /> Bure kujisajili</div>
            <div className="flex items-center gap-2"><CheckCircle className="w-5 h-5 text-green-500" /> Usalama wa NIDA</div>
            <div className="flex items-center gap-2"><CheckCircle className="w-5 h-5 text-green-500" /> Malipo ya M-Pesa</div>
            <div className="flex items-center gap-2"><CheckCircle className="w-5 h-5 text-green-500" /> Msaada 24/7</div>
          </div>
        </div>
      </section>

      {/* NEWS / HABARI ZINAZOJIRI */}
      <section id="news" className="py-20 bg-white dark:bg-gray-950">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between mb-12">
            <div>
              <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-2">
                Habari Zinazojiri
              </h2>
              <p className="text-gray-600 dark:text-gray-400">Habari mpya kila siku</p>
            </div>
            <div className="hidden md:flex items-center gap-2 text-blue-600 font-semibold">
              <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse" />
              LIVE
            </div>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {news.map((n, i) => (
              <article key={i} className="group bg-white dark:bg-gray-900 rounded-2xl border border-gray-200 dark:border-gray-800 overflow-hidden hover:shadow-xl transition-all hover:-translate-y-1">
                <div className="h-40 bg-gradient-to-br from-blue-500 via-indigo-600 to-purple-700 flex items-center justify-center">
                  <Megaphone className="w-16 h-16 text-white/80" />
                </div>
                <div className="p-6">
                  <div className="flex items-center justify-between mb-3">
                    <span className="px-3 py-1 rounded-full bg-blue-100 dark:bg-blue-950 text-blue-700 dark:text-blue-300 text-xs font-bold">{n.tag}</span>
                    <span className="text-xs text-gray-500">{n.date}</span>
                  </div>
                  <h3 className="text-lg font-bold text-gray-900 dark:text-white mb-2 group-hover:text-blue-600 transition">{n.title}</h3>
                  <p className="text-sm text-gray-600 dark:text-gray-400 leading-relaxed mb-4">{n.content}</p>
                  <span className="text-sm font-semibold text-blue-600 dark:text-blue-400 inline-flex items-center gap-1 group-hover:gap-2 transition-all">
                    Soma Zaidi <ArrowRight className="w-4 h-4" />
                  </span>
                </div>
              </article>
            ))}
          </div>
        </div>
      </section>

      {/* FEATURES */}
      <section id="features" className="py-20 bg-gray-50 dark:bg-gray-900">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-4">Vipengele Muhimu</h2>
            <p className="text-lg text-gray-600 dark:text-gray-400">Kila kitu unachohitaji kukopesha kwa ufanisi</p>
          </div>
          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
            {features.map((f, i) => {
              const Icon = f.icon;
              return (
                <div key={i} className="group bg-white dark:bg-gray-950 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 hover:border-blue-500 dark:hover:border-blue-500 transition-all hover:shadow-xl hover:-translate-y-1">
                  <div className={`w-14 h-14 rounded-xl bg-gradient-to-br ${f.color} flex items-center justify-center mb-4 shadow-lg group-hover:scale-110 transition`}>
                    <Icon className="w-7 h-7 text-white" />
                  </div>
                  <h3 className="text-lg font-bold text-gray-900 dark:text-white mb-2">{f.title}</h3>
                  <p className="text-sm text-gray-600 dark:text-gray-400 leading-relaxed">{f.desc}</p>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* HOW IT WORKS */}
      <section id="how" className="py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-4">Jinsi Inavyofanya Kazi</h2>
            <p className="text-lg text-gray-600 dark:text-gray-400">Hatua 4 rahisi kuanza</p>
          </div>
          <div className="grid md:grid-cols-4 gap-6">
            {steps.map((s, i) => (
              <div key={i} className="relative">
                <div className="bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 h-full hover:shadow-xl transition-all hover:-translate-y-1">
                  <div className="w-12 h-12 rounded-full bg-gradient-to-br from-blue-600 to-indigo-600 text-white flex items-center justify-center text-xl font-bold mb-4 shadow-lg">{s.num}</div>
                  <h3 className="text-lg font-bold text-gray-900 dark:text-white mb-2">{s.title}</h3>
                  <p className="text-sm text-gray-600 dark:text-gray-400 leading-relaxed">{s.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* TESTIMONIALS */}
      <section className="py-20 bg-gray-50 dark:bg-gray-900">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-4">Wateja Wanasema Nini</h2>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {testimonials.map((t, i) => (
              <div key={i} className="bg-white dark:bg-gray-950 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 hover:shadow-xl transition-all">
                <div className="flex gap-1 mb-4">
                  {[...Array(5)].map((_, j) => (<Star key={j} className="w-4 h-4 fill-yellow-400 text-yellow-400" />))}
                </div>
                <p className="text-gray-700 dark:text-gray-300 mb-4 leading-relaxed italic">&ldquo;{t.text}&rdquo;</p>
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-gradient-to-br from-blue-600 to-indigo-600 text-white flex items-center justify-center font-bold">{t.name[0]}</div>
                  <div>
                    <p className="font-bold text-gray-900 dark:text-white text-sm">{t.name}</p>
                    <p className="text-xs text-gray-500">{t.role}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CONTACT */}
      <section id="contact" className="py-20 bg-gradient-to-br from-blue-50 to-indigo-50 dark:from-gray-900 dark:to-indigo-950">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-4">📞 Wasiliana Nasi</h2>
            <p className="text-lg text-gray-600 dark:text-gray-400">Tupo tayari kukusaidia wakati wowote</p>
          </div>
          <div className="grid md:grid-cols-3 gap-6 max-w-4xl mx-auto">
            <a href="tel:0694258683" className="group bg-white dark:bg-gray-900 rounded-2xl p-8 border border-gray-200 dark:border-gray-800 hover:shadow-xl transition-all hover:-translate-y-1 text-center">
              <div className="w-16 h-16 rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center mx-auto mb-4 shadow-lg group-hover:scale-110 transition">
                <Phone className="w-8 h-8 text-white" />
              </div>
              <h3 className="font-bold text-gray-900 dark:text-white mb-2">Simu</h3>
              <p className="text-lg font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">0694 258 683</p>
            </a>
            <a href="https://wa.me/255627827053" target="_blank" rel="noopener noreferrer" className="group bg-white dark:bg-gray-900 rounded-2xl p-8 border border-gray-200 dark:border-gray-800 hover:shadow-xl transition-all hover:-translate-y-1 text-center">
              <div className="w-16 h-16 rounded-full bg-gradient-to-br from-green-500 to-emerald-600 flex items-center justify-center mx-auto mb-4 shadow-lg group-hover:scale-110 transition">
                <MessageCircle className="w-8 h-8 text-white" />
              </div>
              <h3 className="font-bold text-gray-900 dark:text-white mb-2">WhatsApp</h3>
              <p className="text-lg font-bold bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">0627 827 053</p>
            </a>
            <div className="bg-white dark:bg-gray-900 rounded-2xl p-8 border border-gray-200 dark:border-gray-800 text-center">
              <div className="w-16 h-16 rounded-full bg-gradient-to-br from-purple-500 to-pink-600 flex items-center justify-center mx-auto mb-4 shadow-lg">
                <MapPin className="w-8 h-8 text-white" />
              </div>
              <h3 className="font-bold text-gray-900 dark:text-white mb-2">Location</h3>
              <p className="text-sm text-gray-600 dark:text-gray-400 font-semibold">Dodoma, Tanzania</p>
            </div>
          </div>

          <div className="mt-12 text-center">
            <p className="text-gray-600 dark:text-gray-400 mb-4 font-semibold">Tufuate kwenye Mitandao</p>
            <div className="flex justify-center gap-4">
              <a href="https://wa.me/255627827053" target="_blank" rel="noopener noreferrer" className="w-12 h-12 rounded-full bg-green-500 hover:bg-green-600 flex items-center justify-center text-white transition shadow-lg hover:scale-110">
                <MessageCircle className="w-6 h-6" />
              </a>
              <a href="#" className="w-12 h-12 rounded-full bg-blue-600 hover:bg-blue-700 flex items-center justify-center text-white transition shadow-lg hover:scale-110">
                <Facebook className="w-6 h-6" />
              </a>
              <a href="#" className="w-12 h-12 rounded-full bg-gradient-to-br from-pink-500 to-orange-500 hover:from-pink-600 hover:to-orange-600 flex items-center justify-center text-white transition shadow-lg hover:scale-110">
                <Instagram className="w-6 h-6" />
              </a>
              <a href="#" className="w-12 h-12 rounded-full bg-sky-500 hover:bg-sky-600 flex items-center justify-center text-white transition shadow-lg hover:scale-110">
                <Twitter className="w-6 h-6" />
              </a>
            </div>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-20 bg-gradient-to-br from-blue-600 via-indigo-600 to-purple-700">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl md:text-4xl font-bold text-white mb-6">Tayari Kuanza?</h2>
          <p className="text-lg text-blue-100 mb-8">Jiunge na maelfu ya wakopeshaji Tanzania wanaotumia JmkLoanApp kila siku.</p>
          <Link href="/register" className="inline-flex items-center gap-2 px-8 py-4 bg-white text-blue-700 font-bold rounded-xl hover:bg-gray-100 transition shadow-xl hover:scale-105 transform">
            Jisajili Bure Sasa
            <ArrowRight className="w-5 h-5" />
          </Link>
        </div>
      </section>

      {/* FOOTER */}
      <footer className="bg-gray-900 text-gray-400 py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-4 gap-8 mb-8">
            <div>
              <div className="flex items-center gap-2 mb-4">
                <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-xl shadow-lg">💰</div>
                <span className="text-xl font-bold bg-gradient-to-r from-blue-400 to-indigo-400 bg-clip-text text-transparent">JmkLoanApp</span>
              </div>
              <p className="text-sm mb-4">Mfumo wa kisasa wa mikopo Tanzania.</p>
              <div className="flex gap-3">
                <a href="tel:0694258683" className="text-gray-400 hover:text-white transition"><Phone className="w-5 h-5" /></a>
                <a href="https://wa.me/255627827053" target="_blank" rel="noopener noreferrer" className="text-gray-400 hover:text-white transition"><MessageCircle className="w-5 h-5" /></a>
              </div>
            </div>
            <div>
              <h4 className="text-white font-bold mb-4">Bidhaa</h4>
              <ul className="space-y-2 text-sm">
                <li><a href="#features" className="hover:text-white transition">Vipengele</a></li>
                <li><a href="#how" className="hover:text-white transition">Jinsi Inavyofanya Kazi</a></li>
                <li><a href="#news" className="hover:text-white transition">Habari</a></li>
              </ul>
            </div>
            <div>
              <h4 className="text-white font-bold mb-4">Kampuni</h4>
              <ul className="space-y-2 text-sm">
                <li><a href="#contact" className="hover:text-white transition">Wasiliana</a></li>
                <li><a href="#" className="hover:text-white transition">Kuhusu Sisi</a></li>
              </ul>
            </div>
            <div>
              <h4 className="text-white font-bold mb-4">Wasiliana</h4>
              <ul className="space-y-2 text-sm">
                <li className="flex items-center gap-2"><Phone className="w-4 h-4" /><a href="tel:0694258683" className="hover:text-white transition">0694 258 683</a></li>
                <li className="flex items-center gap-2"><MessageCircle className="w-4 h-4" /><a href="https://wa.me/255627827053" target="_blank" rel="noopener noreferrer" className="hover:text-white transition">0627 827 053</a></li>
                <li className="flex items-center gap-2"><MapPin className="w-4 h-4" /><span>Dodoma, Tanzania</span></li>
              </ul>
            </div>
          </div>
          <div className="border-t border-gray-800 pt-8 text-center text-sm">
            <p>&copy; 2026 JmkLoanApp. Haki zote zimehifadhiwa. 🇹🇿</p>
          </div>
        </div>
      </footer>
    </main>
  );
}
