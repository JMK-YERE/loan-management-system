'use client';

import { useEffect, useRef, useState } from 'react';

const W = 413; // 35mm @ 300dpi
const H = 531; // 45mm @ 300dpi

function toPassport(src: CanvasImageSource, sw: number, sh: number): string {
  const canvas = document.createElement('canvas');
  canvas.width = W;
  canvas.height = H;
  const ctx = canvas.getContext('2d')!;
  ctx.fillStyle = '#ffffff';
  ctx.fillRect(0, 0, W, H);
  const target = W / H;
  let cw = sw;
  let ch = sh;
  if (sw / sh > target) cw = sh * target;
  else ch = sw / target;
  ctx.drawImage(src, (sw - cw) / 2, (sh - ch) / 2, cw, ch, 0, 0, W, H);
  return canvas.toDataURL('image/jpeg', 0.85);
}

export default function CameraCapture({ value, onChange }: { value: string; onChange: (v: string) => void }) {
  const videoRef = useRef<HTMLVideoElement>(null);
  const streamRef = useRef<MediaStream | null>(null);
  const [active, setActive] = useState(false);
  const [err, setErr] = useState('');

  const stop = () => {
    streamRef.current?.getTracks().forEach((t) => t.stop());
    streamRef.current = null;
    setActive(false);
  };

  useEffect(() => () => { streamRef.current?.getTracks().forEach((t) => t.stop()); }, []);

  useEffect(() => {
    if (active && videoRef.current && streamRef.current) {
      videoRef.current.srcObject = streamRef.current;
      videoRef.current.play().catch(() => {});
    }
  }, [active]);

  const start = async () => {
    setErr('');
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: { facingMode: 'user', width: { ideal: 1280 }, height: { ideal: 960 } },
        audio: false,
      });
      streamRef.current = stream;
      setActive(true);
    } catch {
      setErr('Kamera haikuruhusiwa au haipatikani. Ruhusu kamera kwenye browser, au pakia picha.');
    }
  };

  const snap = () => {
    const v = videoRef.current;
    if (!v || !v.videoWidth) return;
    onChange(toPassport(v, v.videoWidth, v.videoHeight));
    stop();
  };

  const onFile = (e: React.ChangeEvent<HTMLInputElement>) => {
    const f = e.target.files?.[0];
    if (!f) return;
    const url = URL.createObjectURL(f);
    const img = new Image();
    img.onload = () => {
      onChange(toPassport(img, img.naturalWidth, img.naturalHeight));
      URL.revokeObjectURL(url);
    };
    img.onerror = () => setErr('Faili si picha sahihi');
    img.src = url;
  };

  const btn = 'px-4 py-2 rounded-xl text-sm font-semibold transition';

  return (
    <div className="space-y-3">
      <div className="mx-auto w-40 rounded-xl overflow-hidden border-2 border-dashed border-gray-300 dark:border-gray-700 bg-gray-100 dark:bg-gray-800" style={{ aspectRatio: '35 / 45' }}>
        {active ? (
          <video ref={videoRef} playsInline muted className="w-full h-full object-cover" style={{ transform: 'scaleX(-1)' }} />
        ) : value ? (
          // eslint-disable-next-line @next/next/no-img-element
          <img src={value} alt="Picha ya pasipoti" className="w-full h-full object-cover" />
        ) : (
          <div className="w-full h-full flex items-center justify-center text-4xl text-gray-400">👤</div>
        )}
      </div>

      <div className="flex flex-wrap justify-center gap-2">
        {active ? (
          <>
            <button type="button" onClick={snap} className={`${btn} bg-green-600 text-white hover:bg-green-700`}>📸 Piga picha</button>
            <button type="button" onClick={stop} className={`${btn} bg-gray-200 dark:bg-gray-700 text-gray-800 dark:text-gray-100`}>Ghairi</button>
          </>
        ) : (
          <>
            <button type="button" onClick={start} className={`${btn} bg-blue-600 text-white hover:bg-blue-700`}>
              {value ? '🔄 Piga tena' : '📷 Washa kamera'}
            </button>
            <label className={`${btn} cursor-pointer bg-gray-200 dark:bg-gray-700 text-gray-800 dark:text-gray-100`}>
              Pakia picha
              <input type="file" accept="image/*" capture="user" onChange={onFile} className="hidden" />
            </label>
          </>
        )}
      </div>

      <p className="text-xs text-center text-gray-500">
        Simama mbele ya ukuta mweupe au wa rangi moja, uso wote uonekane, bila miwani ya jua au kofia.
      </p>
      {err && <p className="text-xs text-center text-red-600">{err}</p>}
    </div>
  );
}
