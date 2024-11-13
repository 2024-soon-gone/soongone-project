import {
  Image,
  KeyboardAvoidingView,
  ScrollView,
  Text,
  TextInput,
  StyleSheet,
  View,
  ActivityIndicator,
} from 'react-native';
import theme from '../../assets/Theme';
import DoneButton from './Component/DoneBtn';
import api from '../../Utils/API/Axios';
import { useState } from 'react';
import { BASEURL } from '@env';
import { getItem } from '../../Utils/Storage/AsyncStorage';

export default function MintScreen({ route, navigation }) {
  const [text, setText] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const sendImage = async () => {
    const path = route.params.path;
    var filename = path.split('/');
    filename = filename[filename.length - 1];
    console.log(path);
    console.log(filename);
    setIsLoading(true);

    await api
      .post('/post/createPost', {
        text: text,
        location: 'SKKU',
      })
      .then(async (res) => {
        console.log('Create Post Done');

        const formdata = new FormData();
        formdata.append('image', {
          uri: 'file://' + path,
          type: 'image/jpeg',
          name: filename + '.jpg',
        });
        console.log(
          formdata.getParts().find((item) => item.fieldName === 'image'),
        );

        api
          .post('/post/postImage?', formdata, {
            headers: {
              'Content-Type': 'multipart/form-data; boundary="boundary"',
            },
            transformRequest: (data) => data,
          })
          .then((res) => {
            console.log(res.data);
            setIsLoading(false);
            navigation.navigate('Camera');
            navigation.navigate('Home', { refresh: true });
          })
          .catch((err) => console.log(err));
      });
  };

  return (
    <KeyboardAvoidingView style={styles.root}>
      <ScrollView style={{ width: '100%', marginBottom: 20 }}>
        <View style={{ alignItems: 'center' }}>
          <Image
            source={{ uri: 'file://' + route.params.path }}
            style={styles.image}
          />
          <TextInput
            style={styles.input}
            placeholder="지금 이 순간을 문구로 남겨보세요"
            multiline={true}
            onChangeText={(input) => {
              setText(input);
            }}
          />
        </View>
      </ScrollView>
      <DoneButton style={{ marginBottom: 20 }} onPress={sendImage} />
      {isLoading && (
        <View style={styles.laodingView}>
          <ActivityIndicator size={'large'}></ActivityIndicator>
        </View>
      )}
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  root: {
    width: '100%',
    flex: 1,
    alignItems: 'center',
    paddingBottom: 20,
  },
  image: {
    height: 250,
    width: 250,
    backgroundColor: theme.grey6,
    marginBottom: 20,
    borderRadius: 10,
  },
  input: {
    width: '90%',
    borderBottomWidth: 2,
    borderRadius: 10,
    padding: 10,
    textAlignVertical: 'top',
  },
  laodingView: {
    position: 'absolute',
    top: 0,
    left: 0,
    height: '100%',
    width: '100%',
    zIndex: 1,
    backgroundColor: 'rgba(0,0,0,0.6)',
    justifyContent: 'center',
    alignItems: 'center',
  },
});
