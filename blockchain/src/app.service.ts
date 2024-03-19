import { Injectable } from '@nestjs/common';

@Injectable()
export class AppService {
  getHello(): string {
    console.log('soongone-bc-server connected');
    return 'Welcome to SoonGone Blockchain API';
  }
}
