import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';

// Handling BigInt value to string
(BigInt.prototype as any).toJSON = function () {
  return this.toString();
};

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(
    new ValidationPipe({
      disableErrorMessages: false,
      // whitelist:true, // whitelist : set the acceptable properties, properties not included in the whitelist is automatically stripped
    }),
  );
  await app.listen(3000);
}
bootstrap();
