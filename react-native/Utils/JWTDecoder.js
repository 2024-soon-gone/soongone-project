import base64 from 'base-64';

export default function DecodingInfo(jwtToken) {
  //jwt토큰 디코딩
  let payload = jwtToken.substring(
    jwtToken.indexOf('.') + 1,
    jwtToken.lastIndexOf('.'),
  );
  let decodingInfo = base64.decode(payload);
  let decodingInfoJson = JSON.parse(decodingInfo);

  return decodingInfoJson;
}
