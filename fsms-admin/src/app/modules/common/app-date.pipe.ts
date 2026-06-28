import { Pipe, PipeTransform, inject } from '@angular/core';
import { LanguageService } from './language.service';

const LOCALE_BY_LANG: Record<string, string> = {
    en: 'en-GB',
    uk: 'uk-UA',
};

@Pipe({
    name: 'appDate',
    standalone: true,
    pure: false,
})
export class AppDatePipe implements PipeTransform {
    private readonly languageService = inject(LanguageService);

    transform(value: string | number | Date | null | undefined, withTime = true): string {
        if (value === null || value === undefined || value === '') return '';
        const date = value instanceof Date ? value : new Date(value);
        if (Number.isNaN(date.getTime())) return '';
        const locale = LOCALE_BY_LANG[this.languageService.current()] ?? 'en-GB';
        const options: Intl.DateTimeFormatOptions = withTime
            ? { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' }
            : { day: '2-digit', month: 'short', year: 'numeric' };
        return new Intl.DateTimeFormat(locale, options).format(date);
    }
}
