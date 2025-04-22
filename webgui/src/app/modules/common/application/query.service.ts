import { Observable, BehaviorSubject, tap, finalize, catchError, throwError } from "rxjs";

export default class QueryService<T, Args extends any[]> {
    private method: (...args: Args) => Observable<T>;
    
    private dataSubject = new BehaviorSubject<T | null>(null);
    private loadingSubject = new BehaviorSubject<boolean>(false);
    
    data$: Observable<T | null> = this.dataSubject.asObservable();
    loading$: Observable<boolean> = this.loadingSubject.asObservable();
    
    constructor(method: (...args: Args) => Observable<T>) {
        this.method = method;
    }
    
    executeQuery(...args: Args): Observable<T> {
        this.loadingSubject.next(true);
        return this.method(...args)
            .pipe(
                tap((result) => this.dataSubject.next(result)),
                catchError((err) => {
                    console.error("An error occurred: ", err)
                    return throwError(() => new Error("An error occurred while executing query"))
                }),
                finalize(() => this.loadingSubject.next(false)),
            )
    }
    
    get data(): T | null {
        return this.dataSubject.getValue();
    }
    
    get loading(): boolean {
        return this.loadingSubject.getValue();
    }
    
    clearQuery(): void {
        this.loadingSubject.next(false);
        this.dataSubject.next(null);
    }
}