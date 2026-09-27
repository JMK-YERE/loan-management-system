import Link from 'next/link';
import { ArrowRight, Shield, Zap, Users, TrendingUp, CheckCircle, Star } from 'lucide-react';

export default function Home() {
  const announcements = [
    {
      id: 1,
      title: 'Riba 5% kwa Wiki Hii',
      content: 'Pata mkopo kwa riba nafuu ya 5% kwa wiki hii. Ofa ni kwa wateja wapya tu.',
      date: '2026-09-27',
      tag: 'Ofa',
    },
    {
      id: 2,
      title: 'Mfumo Mpya wa Malipo',
      content: 'Sasa unaweza kulipa kwa M-Pesa, Tigo Pesa, Airtel Money na HaloPesa.',
      date: '2026-09-26',
      tag: 'Habari',
    },
    {
      id: 3,
      title: 'Karibu JmkLoanApp',
      content: 'Mfumo wa kisasa wa mikopo kwa wakopeshaji binafsi, VICOBA, na SACCOS Tanzania.',
      date: '2026-09-25',
      tag: 'Matangazo',
    },
  ];

  const features = [
    {
      icon: Shield,
      title: 'Usalama wa Hali ya Juu',
      desc: 'Data yako inalindwa kwa encryption ya kisasa na uthibitisho wa NIDA.',
    },
    {
      icon: Zap,
      title: 'Haraka na Rahisi',
      desc: 'Omba mkopo kwa dakika 2 tu. Uthibitisho unakuja haraka.',
    },
    {
      icon: Users,
      title: 'Wadhamini',
      desc: 'Ongeza wadhamini kwa urahisi. Wanaidhinisha kwa simu.',
    },
    {
      icon: TrendingUp,
      title: 'Ripoti za Kina',
      desc: 'Ona faida, mikopo inayochelewa, na historia yote.',
    },
  ];

  const steps = [
    { num: '1', title: 'Jisajili', desc: 'Jaza taarifa zako — NIDA, email, simu.' },
    { num: '2', title: 'Subiri Uthibitisho', desc: 'Admin anakagua na kukutumia email.' },
    { num: '3', title: 'Weka Password', desc: 'Bonyeza link kwenye email, weka password.' },
    { num: '4', title: 'Anza Kukopesha', desc: 'Ingia, ongeza wakopaji, toa mikopo.' },
  ];

  const testimonials = [
    {
      name: 'Joseph M.',
      role: 'Mkopeshaji',
      text: 'JmkLoanApp imerahisisha biashara yangu. Sasa nafuatilia mikopo yote kwa simu.',
    },
    {
      name: 'Neema K.',
      role: 'Mkopaji',
      text: 'Nilipata mkopo haraka bila usumbufu. Malipo kwa M-Pesa ni rahisi sana.',
    },
    {
      name: 'Amina S.',
      role: 'Mdhamini',
      text: 'Kuidhinisha mikopo kwa wateja wangu ni rahisi. Nashukuru sana.',
    },
  ];

  return (
    <main className="min-h-screen bg-white dark:bg-gray-950">
      {/* NAVBAR */}
      <nav className="sticky top-0 z-50 bg-white/80 dark:bg-gray-950/80 backdrop-blur-lg border-b border-gray-200 dark:border-gray-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <Link href="/" className="flex items-center gap-2">
              <div className="w-10 h-10 rounded-xl bg-blue-600 flex items-center justify-center text-white text-xl">
                💰
              </div>
              <span className="text-xl font-bold text-gray-900 dark:text-white">
                JmkLoanApp
              </span>
            </Link>
            <div className="hidden md:flex items-center gap-8">
              <a href="#features" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 transition">
                Vipengele
              </a>
              <a href="#how" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 transition">
                Jinsi Inavyofanya Kazi
              </a>
              <a href="#announcements" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 transition">
                Matangazo
              </a>
            </div>
            <div className="flex items-center gap-3">
              <Link
                href="/login"
                className="px-4 py-2 text-sm font-semibold text-gray-700 dark:text-gray-200 hover:text-blue-600 transition">
                Ingia
              </Link>
              <Link
                href="/register"
                className="px-5 py-2 text-sm font-semibold bg-blue-600 hover:bg-blue-700 text-white rounded-xl transition shadow-md hover:shadow-lg">
                Jisajili
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* HERO */}
      <section className="relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-blue-50 via-white to-indigo-50 dark:from-gray-950 dark:via-gray-900 dark:to-gray-950" />
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 md:py-32">
          <div className="text-center max-w-3xl mx-auto">
            <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-blue-100 dark:bg-blue-950 text-blue-700 dark:text-blue-300 text-sm font-semibold mb-6">
              <Star className="w-4 h-4" />
              Mfumo #1 wa Mikopo Tanzania
            </div>
            <h1 className="text-4xl md:text-6xl font-extrabold text-gray-900 dark:text-white tracking-tight mb-6">
              Mikopo kwa{' '}
              <span className="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
                Wote Tanzania
              </span>
            </h1>
            <p className="text-lg md:text-xl text-gray-600 dark:text-gray-300 mb-10 leading-relaxed">
              Mfumo wa kisasa wa kukopesha kwa wakopeshaji binafsi, VICOBA, SACCOS, na taasisi ndogo. 
              Rahisi, salama, na wa haraka.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                href="/register"
                className="inline-flex items-center justify-center gap-2 px-8 py-4 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl transition shadow-lg hover:shadow-xl">
                Anza Sasa — Bure
                <ArrowRight className="w-5 h-5" />
              </Link>
              <a
                href="#how"
                className="inline-flex items-center justify-center gap-2 px-8 py-4 border-2 border-gray-300 dark:border-gray-700 hover:border-blue-600 text-gray-700 dark:text-gray-200 font-bold rounded-xl transition">
                Jifunze Zaidi
              </a>
            </div>
          </div>
        </div>
      </section>

      {/* ANNOUNCEMENTS */}
      <section id="announcements" className="py-20 bg-gray-50 dark:bg-gray-900">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-4">
              📢 Matangazo ya Hivi Karibuni
            </h2>
            <p className="text-lg text-gray-600 dark:text-gray-400">
              Habari na ofa mpya kila siku
            </p>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {announcements.map((a) => (
              <div
                key={a.id}
                className="bg-white dark:bg-gray-950 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 hover:shadow-lg transition-shadow">
                <div className="flex items-center justify-between mb-4">
                  <span className="px-3 py-1 rounded-full bg-blue-100 dark:bg-blue-950 text-blue-700 dark:text-blue-300 text-xs font-bold">
                    {a.tag}
                  </span>
                  <span className="text-xs text-gray-500">{a.date}</span>
                </div>
                <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-2">
                  {a.title}
                </h3>
                <p className="text-gray-600 dark:text-gray-400 text-sm leading-relaxed">
                  {a.content}
                </p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* FEATURES */}
      <section id="features" className="py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-4">
              Vipengele Muhimu
            </h2>
            <p className="text-lg text-gray-600 dark:text-gray-400">
              Kila kitu unachohitaji kukopesha kwa ufanisi
            </p>
          </div>
          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
            {features.map((f, i) => {
              const Icon = f.icon;
              return (
                <div
                  key={i}
                  className="bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 hover:border-blue-500 dark:hover:border-blue-500 transition-all hover:shadow-lg">
                  <div className="w-14 h-14 rounded-xl bg-blue-100 dark:bg-blue-950 flex items-center justify-center mb-4">
                    <Icon className="w-7 h-7 text-blue-600 dark:text-blue-400" />
                  </div>
                  <h3 className="text-lg font-bold text-gray-900 dark:text-white mb-2">
                    {f.title}
                  </h3>
                  <p className="text-sm text-gray-600 dark:text-gray-400 leading-relaxed">
                    {f.desc}
                  </p>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* HOW IT WORKS */}
      <section id="how" className="py-20 bg-gray-50 dark:bg-gray-900">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-4">
              Jinsi Inavyofanya Kazi
            </h2>
            <p className="text-lg text-gray-600 dark:text-gray-400">
              Hatua 4 rahisi kuanza
            </p>
          </div>
          <div className="grid md:grid-cols-4 gap-6">
            {steps.map((s, i) => (
              <div key={i} className="relative">
                <div className="bg-white dark:bg-gray-950 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 h-full">
                  <div className="w-12 h-12 rounded-full bg-blue-600 text-white flex items-center justify-center text-xl font-bold mb-4">
                    {s.num}
                  </div>
                  <h3 className="text-lg font-bold text-gray-900 dark:text-white mb-2">
                    {s.title}
                  </h3>
                  <p className="text-sm text-gray-600 dark:text-gray-400 leading-relaxed">
                    {s.desc}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* TESTIMONIALS */}
      <section className="py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-4">
              Wateja Wanasema Nini
            </h2>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {testimonials.map((t, i) => (
              <div
                key={i}
                className="bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800">
                <div className="flex gap-1 mb-4">
                  {[...Array(5)].map((_, j) => (
                    <Star key={j} className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                  ))}
                </div>
                <p className="text-gray-700 dark:text-gray-300 mb-4 leading-relaxed italic">
                  &ldquo;{t.text}&rdquo;
                </p>
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-bold">
                    {t.name[0]}
                  </div>
                  <div>
                    <p className="font-bold text-gray-900 dark:text-white text-sm">
                      {t.name}
                    </p>
                    <p className="text-xs text-gray-500">{t.role}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-20 bg-gradient-to-br from-blue-600 to-indigo-700">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl md:text-4xl font-bold text-white mb-6">
            Tayari Kuanza?
          </h2>
          <p className="text-lg text-blue-100 mb-8">
            Jiunge na maelfu ya wakopeshaji Tanzania wanaotumia JmkLoanApp kila siku.
          </p>
          <Link
            href="/register"
            className="inline-flex items-center gap-2 px-8 py-4 bg-white text-blue-700 font-bold rounded-xl hover:bg-gray-100 transition shadow-lg">
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
                <div className="w-10 h-10 rounded-xl bg-blue-600 flex items-center justify-center text-white text-xl">
                  💰
                </div>
                <span className="text-xl font-bold text-white">JmkLoanApp</span>
              </div>
              <p className="text-sm">
                Mfumo wa kisasa wa mikopo Tanzania.
              </p>
            </div>
            <div>
              <h4 className="text-white font-bold mb-4">Bidhaa</h4>
              <ul className="space-y-2 text-sm">
                <li><a href="#features" className="hover:text-white transition">Vipengele</a></li>
                <li><a href="#how" className="hover:text-white transition">Jinsi Inavyofanya Kazi</a></li>
              </ul>
            </div>
            <div>
              <h4 className="text-white font-bold mb-4">Kampuni</h4>
              <ul className="space-y-2 text-sm">
                <li><a href="#" className="hover:text-white transition">Kuhusu Sisi</a></li>
                <li><a href="#" className="hover:text-white transition">Wasiliana</a></li>
              </ul>
            </div>
            <div>
              <h4 className="text-white font-bold mb-4">Kisheria</h4>
              <ul className="space-y-2 text-sm">
                <li><a href="#" className="hover:text-white transition">Masharti</a></li>
                <li><a href="#" className="hover:text-white transition">Faragha</a></li>
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
