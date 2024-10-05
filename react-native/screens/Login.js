import React, { useEffect } from 'react';
import { View, Text, Pressable, StyleSheet, Image } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import * as Google from 'expo-auth-session/providers/google';
import * as WebBrowser from 'expo-web-browser';
import { GOOGLE_AUTH_CLIENTID } from '@env';
import Logo from '../assets/logo.svg';
import typo from '../assets/Typograph';
import theme from '../assets/Theme';

WebBrowser.maybeCompleteAuthSession();

function Login({ navigation }) {
  const [request, response, promptAsync] = Google.useAuthRequest({
    webClientId: GOOGLE_AUTH_CLIENTID,
  });
  useEffect(() => {
    if (response?.type === 'success') {
      const { authentication } = response;
      console.log(authentication);
      // TODO : 백엔드에 인증받는 로직 -> 이미 유저인 경우 바로 main으로
      //        그렇지 않으면 onboarding으로

      navigation.navigate('InitUserInfo');
    }
  }, [response]);
  return (
    <View style={styles.container}>
      <StatusBar style="auto" />
      <View style={{ marginTop: '156px' }}>
        <Logo />
      </View>
      <Pressable
        style={({ pressed }) => [
          { opacity: pressed ? 0.5 : 1.0, ...styles.btn, marginTop: '120px' },
          styles.defaultStyling,
        ]}
        onPress={() => promptAsync()}
      >
        <Image source={require('../assets/icon/google1.png')}></Image>
        <Text style={{ ...typo.bold, marginStart: 12 }}>구글로 시작하기</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'top',
    backgroundColor: theme.white,
  },
  input: {
    backgroundColor: 'white',
    borderColor: 'black',
    borderBottomWidth: 2,
    width: 300,
    marginBottom: 100,
  },
  btn: {
    borderWidth: 1,
    marginBottom: 10,
    width: 350,
    height: 56,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: '10px',
    backgroundColor: '#FFFFFF',
    borderColor: theme.grey2,
    flexDirection: 'row',
  },
});

export default Login;
