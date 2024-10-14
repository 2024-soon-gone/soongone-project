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
import { setItem, getItem } from '../Utils/Storage/AsyncStorage';
import {
  GoogleSignin,
  statusCodes,
} from '@react-native-google-signin/google-signin';

WebBrowser.maybeCompleteAuthSession();

function Login({ navigation }) {
  const url = `${BASEURL}/oauth2Verify?provider=google&accessToken=`;
  // const [request, response, promptAsync] = Google.useAuthRequest({
  //   webClientId: GOOGLE_AUTH_CLIENTID,
  //   androidClientId: GOOGLE_AUTH_CLIENTID_ANDROID,
  // });
  // useEffect(() => {
  //   if (response?.type === 'success') {
  //     const { authentication } = response;
  //     axios.get(url + authentication.accessToken).then((res) => {
  //       console.log(res.data);

  //       setItem('JWT', res.data.jwtToken);
  //       // if (res.data.isFirst) navigation.navigate('InitUserInfo');
  //       if (true) navigation.navigate('InitUserInfo');
  //       else navigation.reset({ index: 0, routes: [{ name: 'Main' }] });
  //     });
  //   }
  // }, [response]);
  useEffect(() => {
    const checkAndSignIn = async () => {
      const jwt = await getItem('JWT');
      if (jwt !== '') {
        console.log(jwt);
        navigation.reset({ index: 0, routes: [{ name: 'Main' }] });
      }
      GoogleSignin.configure({
        webClientId: GOOGLE_AUTH_CLIENTID,
      });
    };
    checkAndSignIn();
  }, []);

  signIn = async () => {
    try {
      // await GoogleSignin.hasPlayServices();
      const userInfo = await GoogleSignin.signIn();
      const tokens = await GoogleSignin.getTokens();
      if (userInfo.data.idToken) {
        // console.log(userInfo.data);
        console.log(tokens.accessToken);
        console.log(userInfo.data.user.email);
        GoogleSignin.signOut();
        axios.get(url + tokens.accessToken).then((res) => {
          setItem('JWT', res.data.jwtToken);

          if (res.data.isFirst) navigation.navigate('InitUserInfo');
          else navigation.reset({ index: 0, routes: [{ name: 'Main' }] });
        });
      } else {
        throw new Error('no ID token present!');
      }
    } catch (error) {
      if (error.code === statusCodes.SIGN_IN_CANCELLED) {
        // user cancelled the login flow
      } else if (error.code === statusCodes.IN_PROGRESS) {
        // operation (e.g. sign in) is in progress already
      } else if (error.code === statusCodes.PLAY_SERVICES_NOT_AVAILABLE) {
        // play services not available or outdated
      } else {
        // some other error happened
      }
    }
  };
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
        onPress={() => signIn()}
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
