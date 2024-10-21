import {
  View,
  Image,
  Text,
  StyleSheet,
  ScrollView,
  RefreshControl,
  Pressable,
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

const Home = ({ route }) => {
  const [feeds, setFeeds] = useState([]);
  const [refreshing, setRefreshing] = useState(false);
  const [bidding, setBidding] = useState(false);
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState(null);
  const [items, setItems] = useState([
    { label: '1시간', value: '1시간' },
    { label: '12시간', value: '12시간' },
    { label: '하루', value: '하루' },
  ]);

  useEffect(() => {
    api.get('/post').then((res) => {
      console.log(`feeds count: ${res.data.length}`);
      setFeeds(res.data);
    });
  }, []);

  const onRefresh = useCallback(() => {
    setRefreshing(true);
    api.get('/post').then((res) => {
      console.log(`feeds count: ${res.data.length}`);
      setFeeds(res.data);
      setRefreshing(false);
    });
  }, []);

  useFocusEffect(
    useCallback(() => {
      console.log(route.params);
      if (route.params && route.params.refresh) {
        api.get('/post').then((res) => {
          console.log(`feeds count: ${res.data.length}`);
          setFeeds(res.data);
          route.params.refresh = false;
        });
      }
      return;
    }, [route]),
  );

  const onBidPress = (postId) => {
    console.log(`${postId} 거래 제안`);
    setBidding(true);
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
            />
          );
        })}
      </ScrollView>
      {/* 거래 제안 화면 */}
      {bidding && (
        <View style={styles.bidding}>
          <View style={styles.modal}>
            <View
              style={{
                flexDirection: 'row',
                alignItems: 'center',
                justifyContent: 'space-between',
              }}
            >
              <Text style={typo.H4}>거래 제안하기</Text>
              <Pressable onPress={() => setBidding(false)}>
                <Icon name="cancel" size={28} color={theme.red3}></Icon>
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
                  source={{ uri: feeds[0].nftImgIpfsUri }}
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
                    closeOnBackPressed={true}
                    listMode="SCROLLVIEW"
                    scrollViewProps={{
                      nestedScrollEnabled: true,
                    }}
                    style={{ minHeight: 10, borderWidth: 0 }}
                    maxHeight={200}
                    containerStyle={{
                      width: 150,
                      margin: 0,
                      padding: 0,
                    }}
                    textStyle={{
                      fontSize: 14,
                      fontFamily: 'Pretendard-Bold',
                    }}
                    listItemContainerStyle={{
                      height: 30,
                    }}
                  />
                </View>
              </View>
            </ScrollView>
            <View style={{ width: '100%', alignItems: 'center' }}>
              <BidDoneButton />
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
    padding: 16,
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
});

export default Home;
