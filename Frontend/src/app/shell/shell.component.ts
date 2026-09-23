import { Component, signal, computed, OnInit } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map, startWith } from 'rxjs/operators';
import { Router, NavigationEnd, } from '@angular/router';

interface NavItem {
  label: string;
  path: string;
  icon: string;
}

export interface User {
  name: string;
  email: string;
}

@Component({
  selector: 'app-shell',
  templateUrl: './shell.component.html',
  styleUrls: ['./shell.component.css']
})
export class ShellComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.user = localStorage.getItem("userName");
  }
  readonly mobileOpen = signal(false);
  nav: NavItem[] = [
    { label: 'Dashboard', path: '/dashboard', icon: 'M3 13h8V3H3v10zM13 21h8V11h-8v10zM13 3v6h8V3h-8zM3 21h8v-6H3v6z' },
    { label: 'Income', path: '/income', icon: 'M12 19V5M5 12l7-7 7 7' },
    { label: 'Expenses', path: '/expenses', icon: 'M12 5v14M5 12l7 7 7-7' },
    { label: 'Savings', path: '/savings', icon: 'M19 5c-1.5 0-2.8 1.4-3 2-3.5-1.5-11-.3-11 5 0 1.8 0 3 2 4.5V20h4v-2h3v2h4v-4c1-.5 1.7-1 2-2h2v-4h-2c0-1-.5-1.5-1-2V5z' },
  ];

  closeMobile() {
    this.mobileOpen.set(false);
  }

  toggleMobile() {
    this.mobileOpen.update((v) => !v);
  }

  logout() {
    localStorage.removeItem("userName");
    localStorage.removeItem("email");
    this.router.navigate(['/login']);
  }

  readonly firstName = computed(() => this.user?.split(' ')[0] ?? '');
  user: any = "";

  private readonly currentUrl = toSignal(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      map((e) => e.urlAfterRedirects.split('?')[0]),
      startWith(this.router.url.split('?')[0]),
    ),
    { initialValue: this.router.url.split('?')[0] },
  );

  readonly initials = computed(() => {
    const n = this.user ?? '';
    return n
      .split(' ')
      .map((p: any) => p[0])
      .slice(0, 2)
      .join('')
      .toUpperCase();
  });

  readonly pageMeta = computed(() => {
    const url = this.currentUrl();
    const map: Record<string, { title: string; subtitle: string }> = {
      '/dashboard': {
        title: 'Overview',
        subtitle: `Welcome back, ${this.firstName()}`,
      },
      '/income': { title: 'Income', subtitle: 'Track every source of money in' },
      '/expenses': {
        title: 'Expenses',
        subtitle: 'Keep an eye on where money goes',
      },
      '/savings': {
        title: 'Savings',
        subtitle: 'Progress toward your goals',
      },
    };
    return map[url] ?? map['/dashboard'];
  });

}
