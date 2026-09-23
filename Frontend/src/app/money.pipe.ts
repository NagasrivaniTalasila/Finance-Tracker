import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'money', standalone: true })
export class MoneyPipe implements PipeTransform {

  transform(
    value: number | null | undefined,
    fractionDigits = 0
  ): string {

    const n = value ?? 0;

    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      minimumFractionDigits: fractionDigits,
      maximumFractionDigits: fractionDigits
    }).format(n);
  }
  // transform(value: number | null | undefined, fractionDigits = 0): string {
  //   const n = value ?? 0;
  //   return new Intl.NumberFormat('en-US', {
  //     style: 'currency',
  //     currency: 'USD',
  //     minimumFractionDigits: fractionDigits,
  //     maximumFractionDigits: fractionDigits,
  //   }).format(n);
  // }
}