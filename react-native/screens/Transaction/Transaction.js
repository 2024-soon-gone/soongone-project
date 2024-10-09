import { View, StyleSheet, Pressable, Text, ScrollView } from 'react-native';
import { useState } from 'react';
import theme from '../../assets/Theme';
import SendedTransSeg from './Components/SendedTransSeg';
import ReceivedTransSeg from './Components/ReceivedTransSeg';

const data = [
  {
    image:
      'https://previews.123rf.com/images/aquir/aquir1311/aquir131100316/23569861-%EC%83%98%ED%94%8C-%EC%A7%80-%EB%B9%A8%EA%B0%84%EC%83%89-%EB%9D%BC%EC%9A%B4%EB%93%9C-%EC%8A%A4%ED%83%AC%ED%94%84.jpg',
    name: 'sooyoung12',
    price: 12,
    createTime: '2021-05-01 12:20:31',
    validDate: '2021-05-01 12:20:31',
  },
  {
    image:
      'https://previews.123rf.com/images/aquir/aquir1311/aquir131100316/23569861-%EC%83%98%ED%94%8C-%EC%A7%80-%EB%B9%A8%EA%B0%84%EC%83%89-%EB%9D%BC%EC%9A%B4%EB%93%9C-%EC%8A%A4%ED%83%AC%ED%94%84.jpg',
    name: 'sooyoung12',
    price: 12,
    createTime: '2021-05-01 12:20:31',
    validDate: '2021-05-01 12:20:31',
  },
  {
    image:
      'https://previews.123rf.com/images/aquir/aquir1311/aquir131100316/23569861-%EC%83%98%ED%94%8C-%EC%A7%80-%EB%B9%A8%EA%B0%84%EC%83%89-%EB%9D%BC%EC%9A%B4%EB%93%9C-%EC%8A%A4%ED%83%AC%ED%94%84.jpg',
    name: 'sooyoung12',
    price: 12,
    createTime: '2021-05-01 12:20:31',
    validDate: '2021-05-01 12:20:31',
  },
  {
    image:
      'https://previews.123rf.com/images/aquir/aquir1311/aquir131100316/23569861-%EC%83%98%ED%94%8C-%EC%A7%80-%EB%B9%A8%EA%B0%84%EC%83%89-%EB%9D%BC%EC%9A%B4%EB%93%9C-%EC%8A%A4%ED%83%AC%ED%94%84.jpg',
    name: 'sooyoung12',
    price: 12,
    createTime: '2021-05-01 12:20:31',
    validDate: '2021-05-01 12:20:31',
  },
  {
    image:
      'https://previews.123rf.com/images/aquir/aquir1311/aquir131100316/23569861-%EC%83%98%ED%94%8C-%EC%A7%80-%EB%B9%A8%EA%B0%84%EC%83%89-%EB%9D%BC%EC%9A%B4%EB%93%9C-%EC%8A%A4%ED%83%AC%ED%94%84.jpg',
    name: 'sooyoung12',
    price: 12,
    createTime: '2021-05-01 12:20:31',
    validDate: '2021-05-01 12:20:31',
  },
];
const Transaction = () => {
  const [segmentSend, setSegmentSend] = useState(true);
  return (
    <View style={styles.root}>
      <View style={styles.header}>
        <View style={styles.segments}>
          <Pressable
            style={{
              backgroundColor: segmentSend ? 'white' : theme.grey1,
              ...styles.seg,
            }}
            onPress={() => {
              setSegmentSend(true);
            }}
          >
            <Text>보낸 제안</Text>
          </Pressable>
          <Pressable
            style={{
              backgroundColor: segmentSend ? theme.grey1 : 'white',
              ...styles.seg,
            }}
            onPress={() => {
              setSegmentSend(false);
            }}
          >
            <Text>받은 제안</Text>
          </Pressable>
        </View>
      </View>
      <ScrollView style={styles.contents}>
        {segmentSend ? (
          <SendedTransSeg data={data} />
        ) : (
          <ReceivedTransSeg data={data} />
        )}
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
  },
  header: {
    height: 68,
    width: '100%',
    alignItems: 'center',
  },
  segments: {
    flexDirection: 'row',
    backgroundColor: theme.grey1,
    borderRadius: 10,
    width: '90%',
    height: 44,
    padding: 4,
    justifyContent: 'space-evenly',
    gap: 4,
  },
  seg: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 7,
  },
  contents: {
    flex: 1,
    width: '100%',
  },
});

export default Transaction;
