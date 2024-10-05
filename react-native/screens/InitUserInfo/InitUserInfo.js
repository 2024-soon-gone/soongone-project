import React, { useState, createRef, act } from 'react';
import {
  View,
  StyleSheet,
  TextInput,
  Text,
  KeyboardAvoidingView,
} from 'react-native';
import theme from '../../assets/Theme';
import BackButton from './Components/BackBtn';
import NextButton from './Components/NextBtn';
import SkipButton from './Components/SkipBtn';
import DoneButton from './Components/DoneBtn';
import Indicator from './Components/Indicator';

function InitUserInfo({ navigation }) {
  const [step, setStep] = useState(0);
  const [activated, setActivated] = useState(false);
  const [userinfo, setUserinfo] = useState({
    id: '',
    name: '',
    intro: '',
    profileUrl: '',
  });
  const [input, setInput] = useState('');
  const guideSet = [
    '아이디를 입력해주세요',
    '이름을 알려주세요',
    '한 줄 소개를 입력해주세요',
    '프로필 사진을 선택해주세요',
  ];
  const placeholderSet = ['아이디', '이름', '소개'];
  const onChange = (event) => {
    switch (step) {
      case 0:
      case 1:
      case 2:
        let text = event.nativeEvent.text.trim();
        setInput(text);
        if (text.length > 0) {
          setActivated(true);
        } else {
          setActivated(false);
        }
        break;
    }
  };
  const onBackPress = () => {
    if (step > 0) {
      switch (step) {
        case 1:
          setInput(userinfo.id);
          setUserinfo({
            ...userinfo,
            name: input,
          });
          break;
        case 2:
          setInput(userinfo.name);
          setUserinfo({
            ...userinfo,
            intro: input,
          });
          break;
        case 3:
          setInput(userinfo.intro);
          break;
      }
      setStep(step - 1);
      setActivated(true);
    } else {
      navigation.reset({ index: 0, routes: [{ name: 'Login' }] });
    }
  };
  const onPress = () => {
    if (step < 3) {
      switch (step) {
        case 0:
          setUserinfo({
            ...userinfo,
            id: input,
          });
          setInput(userinfo.name);
          setActivated(userinfo.name);
          break;
        case 1:
          setUserinfo({
            ...userinfo,
            name: input,
          });
          setInput(userinfo.intro);
          setActivated(userinfo.intro);
          break;
        case 2:
          setUserinfo({
            ...userinfo,
            intro: input,
          });
          setActivated(false);
          break;
      }
      setStep(step + 1);
    } else {
      setStep(0);
      console.log(userinfo);
    }
  };
  const onDonePress = () => {
    // TODO: backend에 유저 정보 전달
    navigation.navigate('Main');
  };

  return (
    <KeyboardAvoidingView style={styles.rootContainer}>
      <View style={styles.top}>
        <BackButton onPress={onBackPress} />
        {step == 2 && <SkipButton onPress={onPress} />}
        {step == 3 && <DoneButton onPress={onDonePress} />}
      </View>
      <View style={styles.indicator}>
        <Indicator total={4} active={step} />
      </View>
      <Text style={styles.guide}>{guideSet[step]}</Text>
      <View style={{ flex: 1 }}>
        <TextInput
          style={styles.input}
          placeholder={placeholderSet[step]}
          placeholderTextColor={theme.grey3}
          onChange={(text) => onChange(text)}
          value={input}
        />
      </View>
      <View style={{ alignItems: 'center', marginBottom: '20px' }}>
        {step < 3 && <NextButton onPress={onPress} isActivate={activated} />}
      </View>
    </KeyboardAvoidingView>
  );
}
const styles = StyleSheet.create({
  rootContainer: {
    flex: 1,
  },
  top: {
    height: '48px',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: '20px',
  },
  indicator: {
    height: '48px',
    justifyContent: 'center',
    paddingStart: '20px',
  },
  guide: {
    fontSize: '24px',
    color: theme.grey5,
    fontWeight: '700',
    marginLeft: '20px',
  },
  input: {
    fontSize: '24px',
    fontWeight: '700',
    marginTop: '50px',
    marginHorizontal: '20px',
  },
});

export default InitUserInfo;
