import { BehaviorSubject, catchError, finalize, Observable, throwError } from 'rxjs';

export abstract class CommandHandler<TCommand, TResult> {
    private readonly _loading = new BehaviorSubject<boolean>(false);

    readonly loading$ = this._loading.asObservable();

    get loading(): boolean {
        return this._loading.value;
    }

    abstract execute(command: TCommand): Observable<TResult>;

    handle(command: TCommand): Observable<TResult> {
        this._loading.next(true);

        return this.execute(command).pipe(
            catchError(error => {
                console.error(`[${this.constructor.name}] failed:`, error);
                return throwError(() => error);
            }),
            finalize(() => this._loading.next(false))
        );
    }
}
