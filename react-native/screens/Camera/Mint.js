import {
  Image,
  KeyboardAvoidingView,
  ScrollView,
  Text,
  TextInput,
  StyleSheet,
  View,
} from 'react-native';
import theme from '../../assets/Theme';
import DoneButton from './Component/DoneBtn';

export default function MintScreen({ route }) {
  const sendImgae = async () => {
    const result = await fetch(`file://${file.path}`);
    const data = await result.blob();
  };

  return (
    <KeyboardAvoidingView style={styles.root}>
      <ScrollView style={{ width: '100%', marginBottom: 20 }}>
        <View style={{ alignItems: 'center' }}>
          <Image
            //   source={{ uri: 'file://' + route.params.path }}
            style={styles.image}
          />
          <TextInput
            style={styles.input}
            placeholder="지금 이 순간을 문구로 남겨보세요"
            multiline={true}
          />
        </View>
      </ScrollView>
      <DoneButton style={{ marginBottom: 20 }} />
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
});
