import { Pipe, PipeTransform } from '@angular/core';

const CURRENCY_SYMBOLS: Record<string, string> = {
    USD: '$',
    EUR: '€',
    NOK: 'kr',
    UAH: '₴',
};

@Pipe({ name: 'money', standalone: true })
export class MoneyPipe implements PipeTransform {
    transform(amount: number, currency: string): string {
        const symbol = CURRENCY_SYMBOLS[currency] ?? currency;
        return `${Number(amount).toFixed(2)} ${symbol}`;
    }
}
