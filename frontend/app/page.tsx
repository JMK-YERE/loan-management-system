import Link from 'next/link';
import { ArrowRight, Shield, Zap, Users, TrendingUp, Star, Phone, Mail, MapPin, MessageCircle, Facebook, Instagram, Twitter, CheckCircle } from 'lucide-react';

export default function Home() {
  const announcements = [
    {
      id: 1,
      title: 'Riba 5% kwa Wiki Hii',
      content: 'Pata mkopo kwa riba nafuu ya 5% kwa wiki hii. Ofa ni kwa wateja wapya tu.',
      date: '2026-09-27',
      tag: 'Ofa',
      color: 'from-orange-500 to-red-500',
    },
    {
      id: 2,
      title: 'Mfumo Mpya wa Malipo',
      content: 'Sasa unaweza kulipa kwa M-Pesa, Tigo Pesa, Airtel Money na HaloPesa.',
      date: '2026-09-26',
      tag: 'Habari',
      color: 'from-blue-500 to-indigo-600',
    },
    {
      id: 3,
      title: 'Karibu JmkLoanApp',
      content: 'Mfumo wa kisasa wa mikopo kwa wakopeshaji binafsi, VICOBA, na SACCOS Tanzania.',
      date: '2026-09-25',
      tag: 'Matangazo',
      color: 'from-green-500 to-emerald-600',
    },
  ];

  const features = [
    {
      icon: Shield,
      title: 'Usalama wa Hali ya Juu',
      desc: 'Data yako inalindwa kwa encryption ya kisasa na uthibitisho wa NIDA.',
      color: 'from-blue-500 to-indigo-600',
    },
    {
      icon: Zap,
      title: 'Haraka na Rahisi',
      desc: 'Omba mkopo kwa dakika 2 tu. Uthibitisho unakuja haraka.',
      color: 'from-yellow-500 to-orange-500',
    },
    {
      icon: Users,
      title: 'Wadhamini',
      desc: 'Ongeza wadhamini kwa urahisi. Wanaidhinisha kwa simu.',
      color: 'from-green-500 to-emerald-600',
    },
    {
      icon: TrendingUp,
      title: 'Ripoti za Kina',
      desc: 'Ona faida, mikopo inayochelewa, na historia yote.',
      color: 'from-purple-500 to-pink-600',
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
              <a href="#features" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 transition font-medium">
                Vipengele
              </a>
              <a href="#how" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 transition font-medium">
                Jinsi Inavyofanya Kazi
              </a>
              <a href="#announcements" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 transition font-medium">
                Matangazo
              </a>
              <a href="#contact" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 transition font-medium">
                Wasiliana
              </a>
            </div>
            <div className="flex items-center gap-3">
              <Link href="/login" className="px-4 py-2 text-sm font-semibold text-gray-700 dark:text-gray-200 hover:text-blue-600 transition">
                Ingia
              </Link>
              <Link href="/register" className="px-5 py-2 text-sm font-semibold bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white rounded-xl transition shadow-lg hover:shadow-xl">
                Jisajili
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* HERO */}
      <section className="relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-blue-50 via-white to-indigo-100 dark:from-gray-950 dark:via-gray-900 dark:to-indigo-950" />
        <div className="absolute top-0 right-0 w-96 h-96 bg-blue-400/20 rounded-full blur-3xl" />
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-indigo-400/20 rounded-full blur-3xl" />
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 md:py-32">
          <div className="text-center max-w-3xl mx-auto">
            <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-gradient-to-r from-blue-100 to-indigo-100 dark:from-blue-950 dark:to-indigo-950 text-blue-700 dark:text-blue-300 text-sm font-semibold mb-6 shadow-sm">
              <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
              Mfumo #1 wa Mikopo Tanzania
            </div>
            <h1 className="text-4xl md:text-6xl font-extrabold text-gray-900 dark:text-white tracking-tight mb-6 leading-tight">
              Mikopo kwa{' '}
              <span className="bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600 bg-clip-text text-transparent">
                Wote Tanzania
              </span>
            </h1>
            <p className="text-lg md:text-xl text-gray-600 dark:text-gray-300 mb-10 leading-relaxed">
              Mfumo wa kisasa wa kukopesha kwa wakopeshaji binafsi, VICOBA, SACCOS, na taasisi ndogo.
              Rahisi, salama, na wa haraka.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link href="/register" className="inline-flex items-center justify-center gap-2 px-8 py-4 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold rounded-xl transition shadow-lg hover:shadow-xl hover:scale-105 transform">
                Anza Sasa — Bure
                <ArrowRight className="w-5 h-5" />
              </Link>
              <a href="#how" className="inline-flex items-center justify-center gap-2 px-8 py-4 border-2 border-gray-300 dark:border-gray-700 hover:border-blue-600 text-gray-700 dark:text-gray-200 font-bold rounded-xl transition">
                Jifunze Zaidi
              </a>
            </div>
            <div className="mt-10 flex flex-wrap justify-center gap-6 text-sm text-gray-600 dark:text-gray-400">
              <div className="flex items-center gap-2">
                <CheckCircle className="w-5 h-5 text-green-500" />
                Bure kujisajili
              </div>
              <div className="flex items-center gap-2">
                <CheckCircle className="w-5 h-5 text-green-500" />
                Usalama wa NIDA
              </div>
              <div className="flex items-center gap-2">
                <CheckCircle className="w-5 h-5 text-green-500" />
                Malipo ya M-Pesa
              </div>
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
              <div key={a.id} className="group bg-white dark:bg-gray-950 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 hover:shadow-xl transition-all hover:-translate-y-1">
                <div className="flex items-center justify-between mb-4">
                  <span className={`px-3 py-1 rounded-full bg-gradient-to-r ${a.color} text-white text-xs font-bold shadow-md`}>
                    {a.tag}
                  </span>
                  <span className="text-xs text-gray-500">{a.date}</span>
                </div>
                <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-2 group-hover:text-blue-600 transition">
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
                <div key={i} className="group bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 hover:border-blue-500 dark:hover:border-blue-500 transition-all hover:shadow-xl hover:-translate-y-1">
                  <div className={`w-14 h-14 rounded-xl bg-gradient-to-br ${f.color} flex items-center justify-center mb-4 shadow-lg`}>
                    <Icon className="w-7 h-7 text-white" />
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
                <div className="bg-white dark:bg-gray-950 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 h-full hover:shadow-xl transition-all hover:-translate-y-1">
                  <div className="w-12 h-12 rounded-full bg-gradient-to-br from-blue-600 to-indigo-600 text-white flex items-center justify-center text-xl font-bold mb-4 shadow-lg">
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
              <div key={i} className="bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 hover:shadow-xl transition-all">
                <div className="flex gap-1 mb-4">
                  {[...Array(5)].map((_, j) => (
                    <Star key={j} className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                  ))}
                </div>
                <p className="text-gray-700 dark:text-gray-300 mb-4 leading-relaxed italic">
                  &ldquo;{t.text}&rdquo;
                </p>
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-gradient-to-br from-blue-600 to-indigo-600 text-white flex items-center justify-center font-bold">
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

      {/* CONTACT */}
      <section id="contact" className="py-20 bg-gradient-to-br from-blue-50 to-indigo-50 dark:from-gray-900 dark:to-indigo-950">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 dark:text-white mb-4">
              📞 Wasiliana Nasi
            </h2>
            <p className="text-lg text-gray-600 dark:text-gray-400">
              Tupo tayari kukusaidia wakati wowote
            </p>
          </div>
          <div className="grid md:grid-cols-3 gap-6 max-w-4xl mx-auto">
            <a href="tel:0694258683" className="group bg-white dark:bg-gray-900 rounded-2xl p-8 border border-gray-200 dark:border-gray-800 hover:shadow-xl transition-all hover:-translate-y-1 text-center">
              <div className="w-16 h-16 rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center mx-auto mb-4 shadow-lg group-hover:scale-110 transition">
                <Phone className="w-8 h-8 text-white" />
              </div>
              <h3 className="font-bold text-gray-900 dark:text-white mb-2">Simu</h3>
              <p className="text-lg font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
                0694 258 683
              </p>
            </a>
            <a href="https://wa.me/255627827053" target="_blank" rel="noopener noreferrer" className="group bg-white dark:bg-gray-900 rounded-2xl p-8 border border-gray-200 dark:border-gray-800 hover:shadow-xl transition-all hover:-translate-y-1 text-center">
              <div className="w-16 h-16 rounded-full bg-gradient-to-br from-green-500 to-emerald-600 flex items-center justify-center mx-auto mb-4 shadow-lg group-hover:scale-110 transition">
                <MessageCircle className="w-8 h-8 text-white" />
              </div>
              <h3 className="font-bold text-gray-900 dark:text-white mb-2">WhatsApp</h3>
              <p className="text-lg font-bold bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">
                0627 827 053
              </p>
            </a>
            <div className="bg-white dark:bg-gray-900 rounded-2xl p-8 border border-gray-200 dark:border-gray-800 text-center">
              <div className="w-16 h-16 rounded-full bg-gradient-to-br from-purple-500 to-pink-600 flex items-center justify-center mx-auto mb-4 shadow-lg">
                <MapPin className="w-8 h-8 text-white" />
              </div>
              <h3 className="font-bold text-gray-900 dark:text-white mb-2">Location</h3>
              <p className="text-sm text-gray-600 dark:text-gray-400">
                Dar es Salaam, Tanzania
              </p>
            </div>
          </div>

          {/* Social Media */}
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
          <h2 className="text-3xl md:text-4xl font-bold text-white mb-6">
            Tayari Kuanza?
          </h2>
          <p className="text-lg text-blue-100 mb-8">
            Jiunge na maelfu ya wakopeshaji Tanzania wanaotumia JmkLoanApp kila siku.
          </p>
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
                <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-xl shadow-lg">
                  💰
                </div>
                <span className="text-xl font-bold bg-gradient-to-r from-blue-400 to-indigo-400 bg-clip-text text-transparent">
                  JmkLoanApp
                </span>
              </div>
              <p className="text-sm mb-4">
                Mfumo wa kisasa wa mikopo Tanzania.
              </p>
              <div className="flex gap-3">
                <a href="tel:0694258683" className="text-gray-400 hover:text-white transition">
                  <Phone className="w-5 h-5" />
                </a>
                <a href="https://wa.me/255627827053" target="_blank" rel="noopener noreferrer" className="text-gray-400 hover:text-white transition">
                  <MessageCircle className="w-5 h-5" />
                </a>
              </div>
            </div>
            <div>
              <h4 className="text-white font-bold mb-4">Bidhaa</h4>
              <ul className="space-y-2 text-sm">
                <li><a href="#features" className="hover:text-white transition">Vipengele</a></li>
                <li><a href="#how" className="hover:text-white transition">Jinsi Inavyofanya Kazi</a></li>
                <li><a href="#announcements" className="hover:text-white transition">Matangazo</a></li>
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
                <li className="flex items-center gap-2">
                  <Phone className="w-4 h-4" />
                  <a href="tel:0694258683" className="hover:text-white transition">0694 258 683</a>
                </li>
                <li className="flex items-center gap-2">
                  <MessageCircle className="w-4 h-4" />
                  <a href="https://wa.me/255627827053" target="_blank" rel="noopener noreferrer" className="hover:text-white transition">0627 827 053</a>
                </li>
                <li className="flex items-center gap-2">
                  <MapPin className="w-4 h-4" />
                  <span>Dar es Salaam, TZ</span>
                </li>
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
