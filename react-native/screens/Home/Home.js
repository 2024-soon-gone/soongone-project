import {
  View,
  Image,
  Text,
  StyleSheet,
  ScrollView,
  RefreshControl,
  Pressable,
  ActivityIndicator,
} from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import { useCallback, useEffect, useState } from 'react';
import Logo from '../../assets/icon/logo-literal';
import Bell from '../../assets/icon/bell';
import theme from '../../assets/Theme';
import Feed from './Components/Feed';
import api from '../../Utils/API/Axios';
import typo from '../../assets/Typograph';
import { TextInput } from 'react-native-gesture-handler';
import DropDownPicker from 'react-native-dropdown-picker';
import BidDoneButton from './Components/BidDoneBtn';
import Icon from 'react-native-vector-icons/MaterialIcons';
import { getItem } from '../../Utils/Storage/AsyncStorage';

const Home = ({ route, navigation }) => {
  const [feeds, setFeeds] = useState([]);
  const [mySocialId, setMySocialId] = useState('');
  const [refreshing, setRefreshing] = useState(false);
  const [bidding, setBidding] = useState(false);
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState(null);
  const [isBiddingLoading, setIsBiddingLoading] = useState(false);

  // 거래 제안 관련 State
  const [bidPost, setBidPost] = useState({});
  const [items, setItems] = useState([
    { label: '1시간', value: 1 },
    { label: '12시간', value: 12 },
    { label: '하루', value: 24 },
  ]);
  const [bidTokens, setBidTokens] = useState(0);
  const [bidDuration, setBidDuration] = useState('');

  useEffect(() => {
    api.get('/post').then((res) => {
      console.log(`feeds count: ${res.data.length}`);
      const data = res.data.sort((a, b) => b.postDTO.id - a.postDTO.id);
      setFeeds(data);
    });
    getItem('socialUserIdentifier').then((socialId) => setMySocialId(socialId));
  }, []);

  const onRefresh = useCallback(() => {
    setRefreshing(true);
    api.get('/post').then((res) => {
      console.log(`feeds count: ${res.data.length}`);
      const data = res.data.sort((a, b) => b.postDTO.id - a.postDTO.id);
      setFeeds(data);
      setRefreshing(false);
    });
  }, []);

  useFocusEffect(
    useCallback(() => {
      if (route.params && route.params.refresh) {
        api.get('/post').then((res) => {
          console.log(`feeds count: ${res.data.length}`);
          const data = res.data.sort((a, b) => b.postDTO.id - a.postDTO.id);
          setFeeds(data);
          route.params.refresh = false;
        });
      }
      return;
    }, [route]),
  );

  const onBidPress = (postId) => {
    console.log(`${postId} 거래 제안`);
    const selectPost = feeds.filter((post) => post.postDTO.id == postId);
    setBidPost(selectPost[0]);
    setBidding(true);
  };

  const onProposeBidPress = () => {
    setIsBiddingLoading(true);
    const proposeData = {
      amountPaymentToken: 0,
      endTime: '',
      nftId: -1,
    };
    proposeData.amountPaymentToken = parseInt(bidTokens);
    proposeData.endTime = new Date().getTime() + bidDuration * 3600000; // 1000ms * 60sec * 60min
    proposeData.nftId = parseInt(bidPost.postDTO.nftId);
    console.log(proposeData);
    api
      .post('/trade/bid', proposeData)
      .then((res) => {
        console.log(res.data);
        setIsBiddingLoading(false);
        setBidding(false);
        navigation.navigate('Transaction', { refresh: true });
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <View style={styles.root}>
      <View style={styles.header}>
        <Logo />
        <Bell />
      </View>
      <ScrollView
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {feeds.map((feed) => {
          return (
            <Feed
              accountId={feed.postDTO.ownerUser.accountId}
              imageUrl={feed.nftImgIpfsUri}
              likes={feed.postDTO.likes}
              comments={feed.postDTO.comments}
              text={feed.postDTO.text}
              key={feed.postDTO.id}
              onBidPress={onBidPress}
              postId={feed.postDTO.id}
              nftId={feed.postDTO.nftId}
              isMine={mySocialId == feed.postDTO.ownerUser.socialUserIdentifier}
            />
          );
        })}
      </ScrollView>
      {/* 거래 제안 화면 */}
      {bidding && (
        <View style={styles.bidding}>
          <View style={styles.modal}>
            {isBiddingLoading && (
              <View style={styles.biddingLoading}>
                <ActivityIndicator size={'large'} />
              </View>
            )}
            <View style={{ padding: 16, height: '100%' }}>
              <View
                style={{
                  flexDirection: 'row',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                }}
              >
                <Text style={typo.H4}>거래 제안하기</Text>
                <Pressable onPress={() => setBidding(false)}>
                  <Icon name="cancel" size={28} color={theme.negative}></Icon>
                </Pressable>
              </View>
              <ScrollView styles={{ height: '90%', zIndex: 20 }}>
                <View
                  style={{
                    alignItems: 'center',
                    padding: 16,
                    flexDirection: 'row',
                    alignItems: 'flex-start',
                  }}
                >
                  <Image
                    source={{ uri: bidPost.nftImgIpfsUri }}
                    style={{ flex: 1, aspectRatio: 1, resizeMode: 'cover' }}
                  ></Image>
                </View>
                <View style={styles.modalInputContainer}>
                  <Text style={{ ...typo.label_bold, fontSize: 16 }}>
                    거래 가격
                  </Text>
                  <TextInput
                    style={{ flex: 1, marginHorizontal: 4 }}
                    textAlign="right"
                    keyboardType="numeric"
                    onChangeText={setBidTokens}
                  />
                  <Text>ETH</Text>
                </View>
                <View
                  style={{
                    ...styles.modalInputContainer,
                    zIndex: 40,
                    marginBottom: 100,
                  }}
                >
                  <Text
                    style={{
                      ...typo.label_bold,
                      fontSize: 16,
                    }}
                  >
                    유효 기간
                  </Text>
                  <View style={{ zIndex: 500 }}>
                    <DropDownPicker
                      open={open}
                      value={value}
                      items={items}
                      setOpen={setOpen}
                      setValue={setValue}
                      setItems={setItems}
                      onChangeValue={setBidDuration}
                      closeOnBackPressed={true}
                      listMode="SCROLLVIEW"
                      scrollViewProps={{
                        nestedScrollEnabled: true,
                      }}
                      style={{
                        minHeight: 10,
                        borderWidth: 0,
                      }}
                      maxHeight={200}
                      containerStyle={{
                        width: 150,
                        margin: 0,
                        padding: 0,
                      }}
                      textStyle={{
                        fontSize: 14,
                        fontFamily: 'Pretendard-Bold',
                        textAlign: 'right',
                      }}
                      listItemContainerStyle={{
                        height: 30,
                      }}
                      showTickIcon={false}
                    />
                  </View>
                </View>
              </ScrollView>
              <View style={{ width: '100%', alignItems: 'center' }}>
                <BidDoneButton onPress={onProposeBidPress} />
              </View>
            </View>
          </View>
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    backgroundColor: theme.white,
    flex: 1,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
  },
  bidding: {
    backgroundColor: 'rgba(0,0,0,0.6)',
    zIndex: 1,
    height: '100%',
    width: '100%',
    position: 'absolute',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modal: {
    backgroundColor: 'white',
    borderRadius: 10,
    width: '80%',
    height: '80%',
  },
  modalInputContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    borderColor: theme.grey4,
    borderRadius: 10,
    borderWidth: 2,
    height: 40,
    alignItems: 'center',
    paddingHorizontal: 8,
    marginBottom: 16,
  },
  biddingLoading: {
    width: '100%',
    height: '100%',
    zIndex: 999,
    backgroundColor: 'rgba(0,0,0,0.6)',
    position: 'absolute',
    borderRadius: 10,
    justifyContent: 'center',
  },
});

export default Home;
