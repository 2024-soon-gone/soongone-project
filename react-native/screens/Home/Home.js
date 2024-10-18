import { View, Button, Text, StyleSheet, ScrollView } from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import { useCallback, useEffect, useState } from 'react';
import Logo from '../../assets/icon/logo-literal';
import Bell from '../../assets/icon/bell';
import theme from '../../assets/Theme';
import Feed from './Components/Feed';
import api from '../../Utils/API/Axios';

const Home = ({ route }) => {
  const [feeds, setFeeds] = useState([]);

  useEffect(() => {
    api.get('/post').then((res) => {
      console.log(`feeds count: ${res.data.length}`);
      setFeeds(res.data);
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

  return (
    <View style={styles.root}>
      <View style={styles.header}>
        <Logo />
        <Bell />
      </View>
      <ScrollView>
        {feeds.map((feed) => {
          return (
            <Feed
              accountId={feed.postDTO.ownerUser.accountId}
              imageUrl={feed.nftImgIpfsUri}
              likes={feed.postDTO.likes}
              comments={feed.postDTO.comments}
              text={feed.postDTO.text}
              key={feed.postDTO.id}
            />
          );
        })}
      </ScrollView>
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
});

export default Home;
