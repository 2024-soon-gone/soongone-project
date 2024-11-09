export class ResponseDTO<T> {
  constructor(
    public message: string,
    public data: T,
  ) {}
}
