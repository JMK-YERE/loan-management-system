'use client';

import { useState, useEffect } from 'react';
import { Megaphone, ChevronRight } from 'lucide-react';
import Image from 'next/image';
import Link from 'next/link';

interface Announcement {
  id: number;
  title: string;
  content: string;
  tag: string;
  color: string;
  link: string;
  cta: string;
  image: string;
}

export default function AnnouncementTicker() {
  const [current, setCurrent] = useState(0);
  const [isVisible, setIsVisible] = useState(true);

  const announcements: Announcement[] = [
    {
      id: 1,
      title: 'Riba 5% kwa Wiki Hii',
      content: 'Pata mkopo kwa riba nafuu ya 5% kwa wiki hii. Ofa ni kwa wateja wapya tu!',
      tag: '🔥 Ofa',
      color: 'from-orange-500 via-red-500 to-pink-600',
      link: '/register',
      cta: 'Chukua Ofa',
      image: 'https://images.unsplash.com/photo-1556761175-b413da4baf72?w=400&q=80',
    },
    {
      id: 2,
      title: 'Malipo kwa M-Pesa, Tigo, Airtel',
      content: 'Sasa unaweza kulipa kwa M-Pesa, Tigo Pesa, Airtel Money na HaloPesa.',
      tag: '💳 Malipo',
      color: 'from-green-500 via-emerald-600 to-teal-700',
      link: '#features',
      cta: 'Jifunze Zaidi',
      image: 'https://images.unsplash.com/photo-1560250097-0b93528c311a?w=400&q=80',
    },
    {
      id: 3,
      title: 'Uthibitisho wa NIDA',
      content: 'Kila mtumiaji anathibitishwa kwa NIDA. Data yako iko salama kabisa.',
      tag: '🔒 Usalama',
      color: 'from-blue-500 via-indigo-600 to-purple-700',
      link: '/register',
      cta: 'Jisajili',
      image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80',
    },
    {
      id: 4,
      title: 'Msaada 24/7',
      content: 'Tupo tayari kukusaidia wakati wowote. Piga 0694 258 683 au WhatsApp 0627 827 053.',
      tag: '📞 Msaada',
      color: 'from-purple-500 via-pink-600 to-rose-600',
      link: 'https://wa.me/255627827053',
      cta: 'Wasiliana',
      image: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&q=80',
    },
    {
      id: 5,
      title: 'Karibu JmkLoanApp',
      content: 'Mfumo wa kisasa wa mikopo kwa wakopeshaji binafsi, VICOBA, na SACCOS Tanzania.',
      tag: '🎉 Karibu',
      color: 'from-cyan-500 via-blue-600 to-indigo-700',
      link: '/register',
      cta: 'Anza Sasa',
      image: 'https://images.unsplash.com/photo-1516026672322-bc52d61a55d5?w=400&q=80',
    },
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      // Fade out
      setIsVisible(false);
      setTimeout(() => {
        setCurrent((prev) => (prev + 1) % announcements.length);
        setIsVisible(true);
      }, 500);
    }, 10000); // Sekunde 10

    return () => clearInterval(interval);
  }, [announcements.length]);

  const a = announcements[current];

  return (
    <div className={`relative bg-gradient-to-r ${a.color} transition-all duration-500 overflow-hidden`}>
      <div className="absolute inset-0 bg-black/20" />
      <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-3 sm:py-4">
        <div className={`flex items-center justify-between gap-2 sm:gap-4 transition-all duration-500 ${isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-2'}`}>
          {/* Left: Image + Tag + Text */}
          <div className="flex items-center gap-3 flex-1 min-w-0">
            {/* Picha ya Mwafrika */}
            <div className="relative w-12 h-12 sm:w-14 sm:h-14 rounded-full overflow-hidden flex-shrink-0 border-2 border-white/50 shadow-lg">
              <Image
                src={a.image}
                alt={a.title}
                fill
                className="object-cover"
                sizes="56px"
              />
            </div>

            {/* Maandishi */}
            <div className="flex-1 min-w-0">
              <div className="flex flex-wrap items-center gap-2 mb-0.5">
                <span className="px-2 py-0.5 rounded-full bg-white/25 text-white text-[10px] sm:text-xs font-bold whitespace-nowrap">
                  {a.tag}
                </span>
              </div>
              <p className="font-bold text-xs sm:text-sm md:text-base text-white truncate">{a.title}</p>
              <p className="text-[10px] sm:text-xs text-white/90 truncate">{a.content}</p>
            </div>
          </div>

          {/* Right: CTA */}
          <Link href={a.link} className="flex-shrink-0 inline-flex items-center gap-1 px-3 sm:px-4 py-1.5 sm:py-2 bg-white text-gray-900 font-bold rounded-lg hover:bg-gray-100 transition shadow-lg text-[10px] sm:text-xs md:text-sm whitespace-nowrap">
            {a.cta}
            <ChevronRight className="w-3 h-3 sm:w-4 sm:h-4" />
          </Link>
        </div>

        {/* Progress dots */}
        <div className="flex justify-center gap-1.5 mt-2 sm:mt-3">
          {announcements.map((_, i) => (
            <button
              key={i}
              onClick={() => {
                setIsVisible(false);
                setTimeout(() => {
                  setCurrent(i);
                  setIsVisible(true);
                }, 300);
              }}
              className={`h-1 rounded-full transition-all duration-300 ${i === current ? 'w-6 bg-white' : 'w-1.5 bg-white/50 hover:bg-white/80'}`}
              aria-label={`Tangazo ${i + 1}`}
            />
          ))}
        </div>
      </div>
    </div>
  );
}
