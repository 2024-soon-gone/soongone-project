import React, { useEffect } from 'react';
import { View, Text, Pressable, StyleSheet, Image } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import * as Google from 'expo-auth-session/providers/google';
import * as WebBrowser from 'expo-web-browser';
import {
  GOOGLE_AUTH_CLIENTID,
  GOOGLE_AUTH_CLIENTID_ANDROID,
  BASEURL,
} from '@env';
import Logo from '../assets/logo.svg';
import typo from '../assets/Typograph';
import theme from '../assets/Theme';
import axios from 'axios';
import { setItem } from '../Utils/Storage/AsyncStorage';

WebBrowser.maybeCompleteAuthSession();

function Login({ navigation }) {
  const url = `${BASEURL}/oauth2Verify?provider=google&accessToken=`;
  const [request, response, promptAsync] = Google.useAuthRequest({
    webClientId: GOOGLE_AUTH_CLIENTID,
    androidClientId: GOOGLE_AUTH_CLIENTID_ANDROID,
  });
  useEffect(() => {
    if (response?.type === 'success') {
      const { authentication } = response;
      axios.get(url + authentication.accessToken).then((res) => {
        console.log(res.data);
      });
      // navigation.navigate('InitUserInfo');
    }
  }, [response]);
  return (
    <View style={styles.container}>
      <StatusBar style="auto" />
      <View style={{ marginTop: 156 }}>
        <Logo />
      </View>
      <Pressable
        style={({ pressed }) => [
          { opacity: pressed ? 0.5 : 1.0, ...styles.btn, marginTop: 120 },
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
    borderRadius: 10,
    backgroundColor: '#FFFFFF',
    borderColor: theme.grey2,
    flexDirection: 'row',
  },
});

export default Login;
