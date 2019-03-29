package cn.fdongl.meme.seed.controller;


import cn.fdongl.meme.authority.entity.LoginStatus;
import cn.fdongl.meme.pair.mapper.PairMapper;
import cn.fdongl.meme.seed.entity.Flower;
import cn.fdongl.meme.seed.entity.Seed;
import cn.fdongl.meme.seed.entity.SeedStatus;
import cn.fdongl.meme.seed.entity.ShotSeed;
import cn.fdongl.meme.seed.mapper.SeedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/seed")
public class SeedController {

    @Autowired
    SeedMapper seedMapper;

    @Autowired
    PairMapper pairMapper;

    LoginStatus test(){
        return new LoginStatus(62,1165,"");
    }

    @PostMapping("/info")
    public Object info(
            HttpServletRequest httpServletRequest
    ) throws Exception{

        List<Flower>seeds = seedMapper.flowerAll();

        return seeds;
    }

    public int getLevel(int cnt){
        if (cnt<30) return  0;
        else if(cnt<50) return  1;
        else if(cnt<100) return  2;
        else return 3;
    }
    @PostMapping("/unlocked")
    public Object unlocked(
            HttpServletRequest request ,
            @RequestParam("pairId") Integer pairId) throws Exception {

          LoginStatus loginStatus = LoginStatus.fromRequest(request);

          if(pairId == null){
              if(loginStatus.getPairId() == null){
                  throw  new Exception();
                }else {
                  pairId = loginStatus.getPairId();
                }
          }

        Integer cnt = pairMapper.info(pairId).getAPunchCount()+pairMapper.info(pairId).getBPunchCount();

        Integer data =getLevel(cnt);

        return data;
    }

    @PostMapping("/finish")
    public Object finish(
            HttpServletRequest request,
            @RequestParam("pairId")Integer pairId,
            @RequestParam("seedId")Integer seedId) throws  Exception{

        LoginStatus loginStatus = LoginStatus.fromRequest(request);

        if(pairId == null){
            if(loginStatus.getPairId()==null){
                throw new Exception();
            }
            else{
                pairId = loginStatus.getPairId();
            }
        }

        Seed seed = null;
        seed = seedMapper.flower(seedId);
        if(seed == null ) throw new Exception();

        Integer n = null;
        n = seedMapper.rank(seedId);
        if(n < 0){
            throw  new Exception();
        }else {
            seed.setRank(n);
        }

        List<ShotSeed> shotSeed = seedMapper.taskFinish(pairId,seedId);
        seed.setSeeds(shotSeed);
        return seed;
    }

    @PostMapping("/onworking")
    public Object onworking(
                HttpServletRequest request,
                @RequestParam("taskId") Integer taskId
    )throws   Exception{
        LoginStatus loginStatus = LoginStatus.fromRequest(request);

        SeedStatus seedStatus = null;
        seedStatus = seedMapper.status(taskId);

        if(seedStatus == null){
            throw  new Exception();
        }

        return seedStatus;
    }

}
