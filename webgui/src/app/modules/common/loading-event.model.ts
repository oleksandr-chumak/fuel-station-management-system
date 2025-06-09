export class LoadingEvent<T> {
  constructor(public type: T, public value: boolean) {}
}