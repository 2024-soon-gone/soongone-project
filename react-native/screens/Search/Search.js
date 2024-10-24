import {
  View,
  TextInput,
  StyleSheet,
  Dimensions,
  Image,
  ScrollView,
  RefreshControl,
} from 'react-native';
import SearchIcon from '../../assets/icon/search';
import theme from '../../assets/Theme';
import { useState, useEffect, useCallback } from 'react';
import api from '../../Utils/API/Axios';

const WINDOW_WIDTH = Dimensions.get('window').width;
const IMG_WIDTH = (WINDOW_WIDTH - 8) / 3;

const Search = () => {
  const [feeds, setFeeds] = useState([]);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    api.get('/post').then((res) => {
      console.log(`feeds count: ${res.data.length}`);
      const data = res.data.sort((a, b) => b.postDTO.id - a.postDTO.id);
      setFeeds(data);
    });
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

  return (
    <View style={styles.root}>
      <View style={styles.searchBar}>
        <SearchIcon width={20} stroke={theme.grey3} />
        <TextInput
          style={styles.textinput}
          placeholder="검색..."
          placeholderTextColor={theme.grey3}
        ></TextInput>
      </View>
      <ScrollView
        style={{ flex: 1, width: '100%' }}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        <View style={styles.result}>
          {feeds.map((feed) => {
            return (
              <Image
                style={styles.img}
                backgroundColor={theme.red2}
                source={{ uri: feed.nftImgIpfsUri }}
                key={feed.postDTO.id}
              ></Image>
            );
          })}
        </View>
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
  searchBar: {
    flexDirection: 'row',
    height: 57,
    borderColor: theme.grey1,
    borderWidth: 1,
    borderRadius: 6,
    width: '90%',
    height: 36,
    backgroundColor: theme.white,
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 8,
    gap: 8,
    marginBottom: 20,
  },
  textinput: {
    height: 14,
    flex: 1,
  },
  result: {
    width: '100%',
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'flex-start',
    gap: 2,
    padding: 2,
  },
  img: {
    width: IMG_WIDTH,
    height: IMG_WIDTH,
    backgroundColor: theme.red3,
  },
});

export default Search;
