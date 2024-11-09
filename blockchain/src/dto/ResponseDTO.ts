export class ResponseDTO<T> {
  constructor(
    // public resultCode: string, : WIP
    public message: string,
    public response: T,
  ) {}
}
