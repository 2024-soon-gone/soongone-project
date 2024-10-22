import {
  View,
  StyleSheet,
  Pressable,
  Text,
  ScrollView,
  RefreshControl,
  ActivityIndicator,
} from 'react-native';
import { useState, useCallback, useEffect } from 'react';
import theme from '../../assets/Theme';
import SendedTransSeg from './Components/SendedTransSeg';
import ReceivedTransSeg from './Components/ReceivedTransSeg';
import { useFocusEffect } from '@react-navigation/native';
import api from '../../Utils/API/Axios';

const Transaction = ({ route }) => {
  const [segmentSend, setSegmentSend] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [proposedData, setProposedData] = useState([]);
  const [receivedData, setReceivedData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    console.log('거래하기 화면 초기화');
    setIsLoading(true);
    api.get('/trade/nft-bids/proposed').then((res) => {
      console.log(res.data);
      setProposedData(res.data.data.bidsPlacedByUser);
      setIsLoading(false);
    });
  }, []);

  useFocusEffect(
    useCallback(() => {
      if (route.params && route.params.refresh) {
        setSegmentSend(true);
        setIsLoading(true);
        api.get('/trade/nft-bids/proposed').then((res) => {
          setProposedData(res.data.data.bidsPlacedByUser);
          setIsLoading(false);
          route.params.refresh = false;
        });
      }
      return;
    }, [route]),
  );

  const onRefresh = () => {
    setRefreshing(true);
    if (segmentSend) {
      console.log('보낸 제안 새로고침');
      api.get('/trade/nft-bids/proposed').then((res) => {
        console.log(res.data);
        setProposedData(res.data.data.bidsPlacedByUser);
        setRefreshing(false);
      });
    } else {
      console.log('받은 제안 새로고침');
      api.get('/trade/nft-bids/received').then((res) => {
        console.log(res.data);
        setReceivedData(res.data.data.bidsOnNFT);
        setRefreshing(false);
      });
    }
  };

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
              if (proposedData.length > 0) return;
              setIsLoading(true);
              api.get('/trade/nft-bids/proposed').then((res) => {
                console.log(res.data);
                setProposedData(res.data.data.bidsPlacedByUser);
                setIsLoading(false);
              });
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
              if (receivedData.length > 0) return;
              setIsLoading(true);
              api.get('/trade/nft-bids/received').then((res) => {
                console.log(res.data);
                setReceivedData(res.data.data.bidsOnNFT);
                setIsLoading(false);
              });
            }}
          >
            <Text>받은 제안</Text>
          </Pressable>
        </View>
      </View>
      <ScrollView
        style={styles.contents}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {isLoading ? (
          <ActivityIndicator size={'large'} />
        ) : segmentSend ? (
          <SendedTransSeg data={proposedData} />
        ) : (
          <ReceivedTransSeg data={receivedData} />
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
